package de.bund.jki.jki_bonitur.excel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import de.bund.jki.jki_bonitur.BoniturActivity;
import de.bund.jki.jki_bonitur.BoniturSafe;
import de.bund.jki.jki_bonitur.ErrorLog;
import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.MarkerWert;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.db.Versuch;
import de.bund.jki.jki_bonitur.db.VersuchWert;
import de.bund.jki.jki_bonitur.dialoge.WartenDialog;

/**
 * Created by toni.schreiber on 12.06.2015.
 */
public class Reader {
    String mFile;
    HSSFWorkbook mWorkbook;

    public Reader(String file){
        try {
            mFile = file;
            mWorkbook = ExcelLib.openExcelFile( Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/in/"+ mFile);
        }catch (Exception e) {
            Toast.makeText(BoniturSafe.BON_ACTIVITY, e.getMessage(), Toast.LENGTH_LONG).show();
            new ErrorLog(e, BoniturSafe.BON_ACTIVITY);
            BoniturSafe.BON_ACTIVITY.onBackPressed();
            e.printStackTrace();
        }
    }

    public boolean read(){
        if(mWorkbook== null)
            return false;

        Cursor dbCursor = BoniturSafe.db.query(Versuch.TABLE_NAME,new String[]{Versuch.COLUMN_ID},Versuch.COLUMN_NAME+"=?", new String[] {mFile},null,null,null);
        if(dbCursor.getCount()>0){
            dbCursor.moveToFirst();
            BoniturSafe.VERSUCH_NAME = mFile;
            BoniturSafe.VERSUCH_ID = dbCursor.getInt(dbCursor.getColumnIndex(Versuch.COLUMN_ID));
            return true;
        }
        else{
            AsyncTask execute = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {

                    WartenDialog wd = new WartenDialog(BoniturSafe.BON_ACTIVITY);

                    ContentValues values = new ContentValues();
                    Versuch versuch = new Versuch();

                    versuch.name = mFile;

                    versuch.save();

                    BoniturSafe.VERSUCH_ID  = versuch.id;
                    BoniturSafe.VERSUCH_NAME= versuch.name;

                    readMarker();
                    readStandort();
                    readValues();

                    wd.close();

                    Intent i = new Intent(BoniturActivity.RELOAD_VIEW);
                    BoniturSafe.APP_CONTEXT.sendBroadcast(i);

                    return null;
                }
            };

            execute.execute();
            return false;
        }
    }

    private void readMarker() {
        try{
            if(mWorkbook==null){
                Intent i = new Intent();
                i.putExtra("TEXT", "Beim Import der Standorte kam es zu einem Fehler");
                BoniturSafe.APP_CONTEXT.sendBroadcast(i);
                return;
            }
            HSSFSheet sheet = mWorkbook.getSheet("Marker");
            if(sheet == null){
                Toast.makeText(BoniturSafe.BON_ACTIVITY, "Beim Import der Merkmale kam es zu einem Fehler: kein Tabellenblatt Marker gefunden", Toast.LENGTH_LONG).show();
                return;
            }
            int lastRow = sheet.getLastRowNum();
            for (int r = 1; r <= lastRow; r++) {
                HSSFRow row = sheet.getRow(r);
                ContentValues values = new ContentValues();

                values.put(Marker.COLUMN_VERSUCH, BoniturSafe.VERSUCH_ID);

                values.put(Marker.COLUMN_CODE, row.getCell(0).getStringCellValue());
                values.put(Marker.COLUMN_NAME, row.getCell(1).getStringCellValue());
                values.put(Marker.COLUMN_BESCHREIBUNG, !ExcelLib.isCellEmpty(row, 4) ? row.getCell(4).getStringCellValue() : null);
                values.put(Marker.COLUMN_TYPE, (int) row.getCell(2).getNumericCellValue());

                int marker_id = (int) BoniturSafe.db.insert(Marker.TABLE_NAME, null, values);
                if ((int)values.get(Marker.COLUMN_TYPE) == 1) {
                    readMarkerWerte(row, marker_id);
                }
            }
        }catch (Exception e)
        {
            new ErrorLog(e,BoniturSafe.BON_ACTIVITY);
            e.printStackTrace();
        }
    }

    private void readMarkerWerte(HSSFRow row, int marker_id){
        String data = row.getCell(3).getStringCellValue();
        String[] keyValues = data.split(";");

        for(String keyValue : keyValues){
            String[] key_value = keyValue.split(":");
            ContentValues values = new ContentValues();

            values.put(MarkerWert.COLUMN_VALUE, key_value[0]);
            values.put(MarkerWert.COLUMN_LABEL, key_value[1]);

            values.put(MarkerWert.COLUMN_MARKER,marker_id);

            BoniturSafe.db.insert(MarkerWert.TABLE_NAME, null, values);
        }

    }

    private void readStandort(){
        try{
            if(mWorkbook==null){
                Intent i = new Intent();
                i.putExtra("TEXT","Beim Import der Standorte kam es zu einem Fehler");
                BoniturSafe.APP_CONTEXT.sendBroadcast(i);
                return;
            }
            HSSFSheet sheet = mWorkbook.getSheet("Standorte");
            if(sheet == null){
                Intent i = new Intent();
                i.putExtra("TEXT","Beim Import der Standorte kam es zu einem Fehler: kein Tabellenblatt Standorte gefunden");
                BoniturSafe.APP_CONTEXT.sendBroadcast(i);
                return;
            }

            int lastRow = sheet.getLastRowNum();
            for(int r=1; r <= lastRow ; r++) {
                HSSFRow row = sheet.getRow(r);
                Standort standort = new Standort();

                if(ExcelLib.isCellEmpty(row,1)){
                    String[] standortDaten = row.getCell(3).getStringCellValue().split("-");
                    for(int p = 0; p < 3; p++ ){
                        if(standortDaten.length > p){
                            row.createCell(p).setCellValue(Integer.parseInt(standortDaten[p].trim()));
                }}}

                standort.versuchId  = BoniturSafe.VERSUCH_ID;

                standort.parzelle         = ExcelLib.getCellValueString(row,0,true);
                standort.reihe            = (int) row.getCell(1).getNumericCellValue();//Integer.parseInt(tmp[1].replaceAll(" ",""));
                standort.pflanze          = (int) row.getCell(2).getNumericCellValue();
                standort.sorte            = ExcelLib.isCellEmpty(row, 8) ? null : ExcelLib.getCellValueString(row, 8,true);
                standort.zuchtstamm       = ExcelLib.isCellEmpty(row, 9) ? null : ExcelLib.getCellValueString(row, 9,true);
                standort.mutter           = ExcelLib.isCellEmpty(row,10) ? null : ExcelLib.getCellValueString(row,10,true);
                standort.vater            = ExcelLib.isCellEmpty(row,11) ? null : ExcelLib.getCellValueString(row,11,true);
                standort.sortimentsnummer = ExcelLib.isCellEmpty(row,12) ? null : ExcelLib.getCellValueString(row,12,true);
                standort.freifeld         = ExcelLib.isCellEmpty(row,13) ? null : ExcelLib.getCellValueString(row,13,true);

                if(!ExcelLib.isCellEmpty(row,5)){
                    standort.akzessionId = readAkzession(row);
                } else {
                    standort.akzessionId = -1;
                }
                if(!ExcelLib.isCellEmpty(row,7)){
                    standort.passportId = readPassport(row);
                } else {
                    standort.passportId = -1;
                }

                standort.save();
            }
        }catch (Exception e)
        {
            new ErrorLog(e,BoniturSafe.BON_ACTIVITY);
            e.printStackTrace();
        }
    }

    private int readAkzession(HSSFRow row){
        Cursor cursor = BoniturSafe.db.query(Akzession.TABLE_NAME,new String[]{Akzession.COLUMN_ID},Akzession.COLUMN_VERSUCH+"=? AND "+Akzession.COLUMN_NUMMER+"=?",new String[]{""+BoniturSafe.VERSUCH_ID, row.getCell(5).getStringCellValue()},null,null,null);
        if(cursor.getCount()==1)
        {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(Akzession.COLUMN_ID));
        }
        else
        {
            Akzession akzession = new Akzession();
            akzession.versuchId = BoniturSafe.VERSUCH_ID;
            akzession.nummer    = row.getCell(5).getStringCellValue();
            akzession.name      = row.getCell(4).getStringCellValue();

            akzession.save();

            return akzession.id;
        }

    }

    private int readPassport(HSSFRow row){
        Cursor cursor = BoniturSafe.db.query(Passport.TABLE_NAME, new String[]{Passport.COLUMN_ID}, Passport.COLUMN_VERSUCH + "=? AND " + Passport.COLUMN_KENN_NR + "=?", new String[]{"" + BoniturSafe.VERSUCH_ID, ExcelLib.getCellValueString(row, 7, true)}, null, null, null);
        if(cursor.getCount()==1){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(Passport.COLUMN_ID));
        }
        else{
            Passport passport = new Passport();
            passport.versuchId = BoniturSafe.VERSUCH_ID;
            passport.kennNr    = ExcelLib.getCellValueString(row,7,true);
            passport.leitname  = row.getCell(6).getStringCellValue();

            passport.save();

            return passport.id;
        }
    }

    private void readValues(){
        try {
            if(mWorkbook==null){
                Intent i = new Intent();
                i.putExtra("TEXT","Beim Import der Daten kam es zu einem Fehler");
                BoniturSafe.APP_CONTEXT.sendBroadcast(i);
                return;
            }
            HSSFSheet sheet = mWorkbook.getSheet("Daten");
            if (sheet != null) {
                HSSFRow headerRow = sheet.getRow(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                    Standort standort = null;
                    Marker marker = null;

                    HSSFRow row = sheet.getRow(i);
                    String parzelle = ExcelLib.getCellValueString(row, 0, true);
                    String reihe = ExcelLib.getCellValueString(row, 1, true);
                    String pflanze = ExcelLib.getCellValueString(row, 2, true);

                    Cursor c = BoniturSafe.db.query(Standort.TABLE_NAME, new String[]{Standort.COLUMN_ID},
                            Standort.COLUMN_PARZELLE + " = ? AND " + Standort.COLUMN_REIHE + " = ? AND " + Standort.COLUMN_PFLANZE + " = ? AND " + Standort.COLUMN_VERSUCH + " = ?",
                            new String[]{parzelle, reihe, pflanze, BoniturSafe.VERSUCH_ID + ""},
                            null, null, null);

                    if (c.getCount() == 1) {
                        c.moveToFirst();
                        standort = Standort.findByPk(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                    }

                    int step = 1;
                    for (int col = 9; col <= row.getLastCellNum(); col+=step) {
                        step = 1;
                        String value = ExcelLib.getCellValueString(row, col, true);
                        if (!(value == null || value == "")) {
                            String code = ExcelLib.getCellValueString(headerRow, col);
                            String code2= ExcelLib.getCellValueString(headerRow, col+1);
                            String datum = null;
                            if(code2 == null){
                                datum = ExcelLib.getCellValueString(row, col+1, true);
                                if(!datum.contains("."))
                                    datum = null;

                            }
                            if (!(code == null || value == "")) {
                                c = BoniturSafe.db.query(
                                        Marker.TABLE_NAME,
                                        new String[]{Marker.COLUMN_ID},
                                        Marker.COLUMN_CODE + " = ? AND " + Marker.COLUMN_VERSUCH + " = ?",
                                        new String[]{code, BoniturSafe.VERSUCH_ID + ""},
                                        null, null, null
                                );

                                if (c.getCount() == 1) {
                                    c.moveToFirst();
                                    marker = Marker.findByPk(c.getInt(c.getColumnIndex(Marker.COLUMN_ID)));
                                }

                                if (marker != null && standort != null) {
                                    VersuchWert vw = new VersuchWert();
                                    vw.versuchId = BoniturSafe.VERSUCH_ID;
                                    vw.standortId = standort.id;
                                    vw.markerId = marker.id;

                                    if (marker.type != Marker.MARKER_TYPE_BONITUR) {
                                        switch (marker.type) {
                                            case Marker.MARKER_TYPE_MESSEN:
                                                vw.wert_int = Integer.parseInt(value);
                                                break;
                                            case Marker.MARKER_TYPE_BBCH:
                                            case Marker.MARKER_TYPE_DATUM:
                                            case Marker.MARKER_TYPE_BEMERKUNG:
                                                vw.wert_text = value;
                                                break;
                                        }
                                        if(datum!=null)
                                            vw.wert_datum = datum;
                                        vw.save();
                                    } else {
                                        String[] werte = value.split("|");
                                        for (String wert : werte) {
                                            for (MarkerWert mw : marker.werte) {
                                                if (mw.value.compareTo(wert) == 0) {
                                                    vw.wert_id = mw.id;
                                                    if(datum != null)
                                                        vw.wert_datum = datum;
                                                    vw.save();
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            new ErrorLog(e, BoniturSafe.APP_CONTEXT);
        }
    }

}
