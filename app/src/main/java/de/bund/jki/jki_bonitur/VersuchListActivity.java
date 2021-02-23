package de.bund.jki.jki_bonitur;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.BbchArt;
import de.bund.jki.jki_bonitur.db.BbchMainStadium;
import de.bund.jki.jki_bonitur.db.BbchStadium;
import de.bund.jki.jki_bonitur.db.BoniturDatenbank;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.db.Versuch;
import de.bund.jki.jki_bonitur.db.VersuchWert;
import de.bund.jki.jki_bonitur.dialoge.SettingsDialog;
import de.bund.jki.jki_bonitur.excel.ExcelLib;


public class VersuchListActivity extends Activity {

    public  Versuch[] versuche;
    public  Versuch   delVersuch;
    private Context   context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versuch_list);
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFiles();
        init_typefaces();
        loadNewBbch();

        Resources  res = getResources();
    }

    private void loadNewBbch(){
        try {
            String   appPath      = Environment.getExternalStorageDirectory().getAbsolutePath() + Config.BaseFolder;
            String   bbchPath     = appPath + File.separator + "bbch" + File.separator;
            File     bbchFolder   = new File(bbchPath);
            String[] bbchFileList = bbchFolder.list();
            if (bbchFileList != null) {
                ContentValues cv = new ContentValues();

                SQLiteDatabase db = BoniturSafe.db;
                db.beginTransaction();
                boolean success = true;

                //bbch_template.xls + x
                for (String filename : bbchFileList) {
                    if (filename.compareTo("bbch_template.xls") != 0 && filename.endsWith("xls")) {
                        try {
                            createNewBbchEntries(bbchPath, filename, db);
                        } catch (Exception e) {
                            success = false;
                            new ErrorLog(e, this);
                        }
                        File file = new File(bbchPath + filename);
                        file.delete();
                    }

                }
                if (success) {
                    db.setTransactionSuccessful();
                }
                db.endTransaction();
            }
        } catch (Exception e) {
            new ErrorLog(e, getApplication());
        }
    }

    private void createNewBbchEntries(String bbchPath, String filename, SQLiteDatabase db) throws IOException {
        ContentValues cv          = new ContentValues();
        HSSFWorkbook  workbook    = ExcelLib.openExcelFile(bbchPath + filename);
        HSSFSheet     stagesSheet = workbook.getSheetAt(0);
        HSSFRow       row         = stagesSheet.getRow(1);
        String[]      names       = getNamesFromRow(row);
        cv.put(BbchArt.COLUMN_NAME_EN, names[0]);
        cv.put(BbchArt.COLUMN_NAME_DE, names[1]);
        long new_art_id = db.insertOrThrow(BbchArt.TABLE_NAME,null, cv);

        createMainStages(db, workbook, stagesSheet, new_art_id);
    }

    private void createMainStages(SQLiteDatabase db, HSSFWorkbook workbook, HSSFSheet stagesSheet, long new_art_id) throws IOException {
        HSSFRow       row;
        ContentValues cv = new ContentValues();

        for(int stage = 0; stage < 10; stage++){
            row = stagesSheet.getRow(4 + stage);
            String[] names = getNamesFromRow(row);
            String imageFile = row.getLastCellNum() >= 4 && row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";

            if(names[0].length() != 0 || names[1].length() != 0){
                cv = new ContentValues();

                cv.put(BbchMainStadium.COLUMN_ART_ID, "" + new_art_id);
                cv.put(BbchMainStadium.COLUMN_NUMBER, "" + stage);
                cv.put(BbchMainStadium.COLUMN_NAME_EN, names[0]);
                cv.put(BbchMainStadium.COLUMN_NAME_DE, names[0]);
                if(imageFile.length() > 0){
                    copyAndInsertFile(new_art_id, cv, imageFile);
                }

                long new_main_stadium_id = db.insertOrThrow(BbchMainStadium.TABLE_NAME, null, cv);
                createStages(db, workbook, stage, new_main_stadium_id);
            }
        }
    }

    private void copyAndInsertFile(long new_art_id, ContentValues cv, String imageFile) throws IOException {
        String appPath  = Environment.getExternalStorageDirectory().getAbsolutePath() + Config.BaseFolder;
        String bbchPath = appPath + File.separator + "bbch" + File.separator;
        String src = bbchPath + File.separator + imageFile;
        String destBasePath = "/data/data/de.bund.jki.jki_bonitur/images/bbch/";
        String dest = destBasePath + new_art_id + "/" + imageFile;

        File destFolder = new File(destBasePath + new_art_id);
        if(!destFolder.exists()){
            destFolder.mkdirs();
        }

        File srcFile = new File(src);

        if(srcFile.exists()) {
            copyFile(src, dest);
            srcFile.delete();
            cv.put(BbchMainStadium.COLUMN_IMAGE, imageFile);
        }
    }

    private void copyFile(String src, String dest) throws IOException {
        FileInputStream  inStream   = new FileInputStream(src);
        FileOutputStream outStream  = new FileOutputStream(dest);
        FileChannel      inChannel  = inStream.getChannel();
        FileChannel      outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    private void createStages(SQLiteDatabase db, HSSFWorkbook workbook, int stage, long new_main_stadium_id) {
        HSSFRow       row;
        String[]      names;
        ContentValues cv         = new ContentValues();
        HSSFSheet     stageSheet = workbook.getSheetAt(1 + stage);

        for(int stage_row = 0; stage_row < 10; stage_row++){
            row = stageSheet.getRow(1+ stage_row);
            names = getNamesFromRow(row);
            if(names[0].length() != 0 || names[1].length() != 0){
                cv = new ContentValues();

                cv.put(BbchStadium.COLUMN_MAIN_ID, "" + new_main_stadium_id);
                cv.put(BbchStadium.COLUMN_NUMBER, "" + stage_row);
                cv.put(BbchStadium.COLUMN_NAME_EN, names[0]);
                cv.put(BbchStadium.COLUMN_NAME_DE, names[1]);

                db.insertOrThrow(BbchStadium.TABLE_NAME, null, cv);
            }
        }
    }

    private String[] getNamesFromRow(HSSFRow row) {
        String[] names = new String[2];
        names[0] = row.getLastCellNum() >= 2 && row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
        names[1] = row.getLastCellNum() >= 3 && row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";

        return names;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_versuch_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFiles() {
        try {
            if (
                    ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        4711
                );
            }

            Config.load(this);
            String appPath   = Environment.getExternalStorageDirectory().getAbsolutePath() + Config.BaseFolder;
            String path      = appPath + File.separator + "in" + File.separator;
            String bbchPath  = appPath + File.separator + "bbch" + File.separator;
            File   appFolder = new File(appPath);
            File   sd        = Environment.getExternalStorageDirectory();
            File   folder    = new File(path);
            File   bbchFolder = new File(bbchPath);


            if (! appFolder.exists()) {
                Log.v("Files", "Create AppFolder: " + appFolder.mkdir() + "(" + appPath + ")");
                initFolderStructure();
            }
            if (! folder.exists()) {
                Log.v("Files", "Create InFolder: " + folder.mkdir() + " (" + path + ")");
            }

            Log.v("Files", folder.exists() + "");
            Log.v("Files", folder.isDirectory() + "");
            Log.v("Files", folder.listFiles() + "");
            Log.v("Files", "BBCH-Folder: " + bbchFolder.listFiles());

            createFileList(folder);
            createStartedList();

        } catch (Exception e) {
            new ErrorLog(e, getApplication());
        }
    }

    private void createStartedList() {
        BoniturDatenbank boniturDatenbank = new BoniturDatenbank(this);
        SQLiteDatabase   db               = boniturDatenbank.getReadableDatabase();
        BoniturSafe.db = db;

        Cursor c = null;
        try {
            c = db.query(Versuch.TABLE_NAME, new String[]{Versuch.COLUMN_ID}, null, null, null, null, null);

            versuche = new Versuch[c.getCount()];
            int v = 0;
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    versuche[v] = Versuch.findByPk(c.getInt(c.getColumnIndex(Versuch.COLUMN_ID)));
                    v++;
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            new ErrorLog(e, getApplication());
        } finally {
            if (c != null)
                c.close();
        }


        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.jki_bonitur_file_list, android.R.id.text1, versuche) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                try {
                    View view = super.getView(position, convertView, parent);
                    ((TextView) view.findViewById(android.R.id.text1)).setText(versuche[position].name);
                    view.findViewById(R.id.btnDelete).setTag(versuche[position]);

                    return view;
                } catch (Exception e) {
                    new ErrorLog(e, context);
                }
                return null;
            }
        };

        ListView lv = findViewById(R.id.lvDateiBegonnen);
        lv.setAdapter(arrayAdapter2);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(android.R.id.text1);
                Intent   i  = new Intent(context, BoniturActivity.class);
                i.putExtra("FILE", tv.getText().toString());
                context.startActivity(i);
            }
        });
    }

    private void createFileList(File folder) {
        File[]            files = folder.listFiles();
        ArrayList<String> list  = new ArrayList<String>();

        for (int i = 0; i < files.length; ++ i) {
            list.add(files[i].getName());
        }

        ListView lv = findViewById(R.id.lvDateien);

        ArrayAdapter adapter = new ArrayAdapter(this,
                                                android.R.layout.simple_list_item_1, list
        );
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(android.R.id.text1);
                Intent   i  = new Intent(context, BoniturActivity.class);
                i.putExtra("FILE", tv.getText().toString());
                context.startActivity(i);
            }
        });
    }

    public void loadSettings() {
        Config.load(this);
        showFiles();

    }

    public void onClick(final View v) {
        if (v.getId() == R.id.btnSettingList) {
            new SettingsDialog(this);
        } else if (v.getId() == R.id.btnImpressum) {
            Intent i = new Intent(this.context, ImpressumActivity.class);
            this.context.startActivity(i);
        }

    }

    public void init_typefaces() {
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome.ttf");
        int[] buttonIds = new int[]{
                R.id.btnSettingList,
                R.id.btnImpressum
        };

        for (int id : buttonIds) {
            ((Button) this.findViewById(id)).setTypeface(typeface);
        }
    }

    private void initFolderStructure(){
        String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
            Config.BaseFolder + File.separator;

        String path      = basePath + "in" + File.separator;
        File   filePath = new File(path);
        filePath.mkdir();

        path      = basePath + "out" + File.separator;
        filePath = new File(path);
        filePath.mkdir();

        path      = basePath + "bbch" + File.separator;
        filePath = new File(path);
        filePath.mkdir();

        copyBbchTemplate(path);

        path      = basePath + "error" + File.separator;
        filePath = new File(path);
        filePath.mkdir();
    }

    private void copyBbchTemplate(String path) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try{
            reader = new BufferedReader(
                new InputStreamReader(
                    getAssets().open("template" + File.separator  + "bbch_template.xls"))
            );
            FileOutputStream fos = new FileOutputStream(path + "bbch_template.xls");
            writer =  new BufferedWriter(new OutputStreamWriter(fos));
            int character;
            while ((character = reader.read()) != -1) {
                writer.write(character);
            }
        } catch (Exception e){
            new ErrorLog(e, this);
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (Exception e){
                    new ErrorLog(e, this);
                }
            }
            if(writer != null){
                try{
                    writer.close();
                }catch (Exception e){
                    new ErrorLog(e, this);
                }
            }
        }
    }

    public void onClickBoniturDelete(final View v) {
        delVersuch = (Versuch) v.getTag();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BoniturSafe.db.delete(Passport.TABLE_NAME, Passport.COLUMN_VERSUCH + "=?", new String[]{"" + delVersuch.id});
                        BoniturSafe.db.delete(Akzession.TABLE_NAME, Akzession.COLUMN_VERSUCH + "=?", new String[]{"" + delVersuch.id});
                        BoniturSafe.db.delete(VersuchWert.TABLE_NAME, VersuchWert.COLUMN_VERSUCH + "=?", new String[]{"" + delVersuch.id});
                        BoniturSafe.db.delete(Standort.TABLE_NAME, Standort.COLUMN_VERSUCH + "=?", new String[]{"" + delVersuch.id});
                        BoniturSafe.db.delete(Marker.TABLE_NAME, Marker.COLUMN_VERSUCH + "=?", new String[]{"" + delVersuch.id});
                        BoniturSafe.db.delete(Versuch.TABLE_NAME, Versuch.COLUMN_ID + "=?", new String[]{"" + delVersuch.id});

                        showFiles();

                        dialog.dismiss();
                    }
                })
                .setTitle("Versuche \"" + delVersuch.name + "\" löschen ?")
                .setMessage("Sind die sicher? Alle bereits erhobenen Werte gehen verloren!")
                .create();

        dialog.show();


    }
}
