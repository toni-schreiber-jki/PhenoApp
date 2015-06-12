package de.bund.jki.jki_bonitur.excel;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.animation.BounceInterpolator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import de.bund.jki.jki_bonitur.BoniturSafe;
import de.bund.jki.jki_bonitur.db.Versuch;

/**
 * Created by toni.schreiber on 12.06.2015.
 */
public class Reader {
    String mFile;
    HSSFWorkbook mWorkbook;



    Reader(String file)
    {
        try {
            mFile = file;
            mWorkbook = ExcelLib.openExcelFile(mFile);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read()
    {
        Cursor dbCursor = BoniturSafe.db.query(Versuch.TABLE_NAME,new String[]{Versuch.COLUMN_ID},Versuch.COLUMN_NAME+"=?", new String[] {mFile},null,null,null);
        if(dbCursor.getCount()>0){

        }
        else{
            ContentValues values = new ContentValues();
            values.put(Versuch.COLUMN_NAME, mFile);

            BoniturSafe.VERSUCH_ID  = (int) BoniturSafe.db.insert(Versuch.TABLE_NAME,null,values);
            BoniturSafe.VERSUCH_NAME= mFile;
        }
    }

    private void readMarker()
    {
        HSSFSheet sheet = mWorkbook.getSheet("Marker");
        int lastRow = sheet.getLastRowNum();
        for(int r=0; r < lastRow ; r++)
        {
            HSSFRow row = sheet.getRow(r);
        }
    }
}
