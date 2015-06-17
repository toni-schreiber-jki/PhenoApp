package de.bund.jki.jki_bonitur.excel;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;
import android.view.animation.BounceInterpolator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import de.bund.jki.jki_bonitur.BoniturSafe;
import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.MarkerWert;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.db.Versuch;

/**
 * Created by toni.schreiber on 12.06.2015.
 */
public class Reader {
    String mFile;
    HSSFWorkbook mWorkbook;

    public Reader(String file)
    {
        try {
            mFile = file;
            mWorkbook = ExcelLib.openExcelFile( Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/in/"+ mFile);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read()
    {
        Cursor dbCursor = BoniturSafe.db.query(Versuch.TABLE_NAME,new String[]{Versuch.COLUMN_ID},Versuch.COLUMN_NAME+"=?", new String[] {mFile},null,null,null);
        if(dbCursor.getCount()>0){
            dbCursor.moveToFirst();
            BoniturSafe.VERSUCH_NAME = mFile;
            BoniturSafe.VERSUCH_ID = dbCursor.getInt(dbCursor.getColumnIndex(Versuch.COLUMN_ID));
        }
        else{
            ContentValues values = new ContentValues();
            Versuch versuch = new Versuch();

            versuch.name = mFile;

            versuch.save();

            BoniturSafe.VERSUCH_ID  = versuch.id;
            BoniturSafe.VERSUCH_NAME= versuch.name;

            readMarker();
            readStandort();
        }
    }

    private void readMarker() {
        try
        {
            HSSFSheet sheet = mWorkbook.getSheet("Marker");
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
                if (values.get(Marker.COLUMN_TYPE) == 1) {
                    readMarkerWerte(row, marker_id);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void readMarkerWerte(HSSFRow row, int marker_id)
    {
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
        HSSFSheet sheet = mWorkbook.getSheet("Standorte");
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

            if(!ExcelLib.isCellEmpty(row,5)){
                standort.akzessionId = readAkzession(row);
            }
            if(!ExcelLib.isCellEmpty(row,7)){
                standort.passportId = readPassport(row);
            }

            standort.save();
        }
    }

    private int readAkzession(HSSFRow row)
    {
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

    private int readPassport(HSSFRow row)
    {
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

}
