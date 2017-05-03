package de.bund.jki.jki_bonitur;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.BoniturDatenbank;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.db.Versuch;
import de.bund.jki.jki_bonitur.db.VersuchWert;
import de.bund.jki.jki_bonitur.dialoge.SettingsDialog;
import de.bund.jki.jki_bonitur.tools.DateTool;


public class VersuchListActivity extends Activity {

    private Context context;
    public Versuch[] versuche;

    public Versuch delVersuch;


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

    private void showFiles()
    {
        try {

            // <editor-fold desc="In-Ordner">
            Config.load(this);
            String path = Environment.getExternalStorageDirectory().toString() + Config.BaseFolder +  "/in/";
            File folder = new File(path);
            //folder.mkdirs();

            File[] files = folder.listFiles();
            ArrayList<String> list = new ArrayList<String>();

            if(list == null) {
                return;
                //ToDo: Fehlermeldung keine Dateien gefunden
            }

            for (int i = 0; i < files.length; ++i) {
                list.add(files[i].getName());
            }

            ListView lv = (ListView) findViewById(R.id.lvDateien);

            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    Intent i = new Intent(context, BoniturActivity.class);
                    i.putExtra("FILE",tv.getText().toString());
                    context.startActivity(i);
                }
            });
            // </editor-fold>

            // <editor-fold desc="begonnen Bonituren">
            BoniturDatenbank boniturDatenbank = new BoniturDatenbank(this);
            SQLiteDatabase db = boniturDatenbank.getReadableDatabase();
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
            }catch (Exception e){
                new ErrorLog(e,getApplication());
            }finally {
                if ( c != null )
                    c.close();
            }


            ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.jki_bonitur_file_list, android.R.id.text1, versuche) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    try {
                        View view = super.getView(position, convertView, parent);
                        ((TextView) view.findViewById(android.R.id.text1)).setText(versuche[position].name);
                        ((Button) view.findViewById(R.id.btnDelete)).setTag(versuche[position]);

                        return view;
                    } catch (Exception e) {
                        new ErrorLog(e, context);
                    }
                    return null;
                }
            };

            lv = (ListView) findViewById(R.id.lvDateiBegonnen);
            lv.setAdapter(arrayAdapter2);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    Intent i = new Intent(context, BoniturActivity.class);
                    i.putExtra("FILE",tv.getText().toString());
                    context.startActivity(i);
                }
            });

            // </editor-fold>

        }catch (Exception e){
            new ErrorLog(e,getApplication());
        }
    }

    public void loadSettings()
    {
        Config.load(this);
        showFiles();

    }

    public void onClick(final View v)
    {
        if(v.getId() == R.id.btnSettingList){
            new SettingsDialog(this);
        }
    }

    public void init_typefaces() {
        Typeface typeface = Typeface.createFromAsset(this.getAssets(),"fonts/fontawesome.ttf");
        int[] buttonIds = new int[]{
                R.id.btnSettingList,
        };

        for (int id: buttonIds) {
            ((Button) this.findViewById(id)).setTypeface(typeface);
        }
    }

    public void onClickBoniturDelete(final View v){
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
