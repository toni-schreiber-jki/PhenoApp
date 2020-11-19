package de.bund.jki.jki_bonitur.db.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.db.BbchMainStadium;
import de.bund.jki.jki_bonitur.db.BbchStadium;

public class BbchDataImport {
    public static void insertValuesTo(SQLiteDatabase database){
        for (Object[] mainstadium: BbchGrape.data){
            ContentValues values = new ContentValues();

            values.put(BbchMainStadium.COLUMN_ART_ID, mainstadium[0].toString());
            values.put(BbchMainStadium.COLUMN_NUMBER, mainstadium[1].toString());
            values.put(BbchMainStadium.COLUMN_NAME_EN, mainstadium[2].toString());
            values.put(BbchMainStadium.COLUMN_NAME_DE, mainstadium[3].toString());
            values.put(BbchMainStadium.COLUMN_IMAGE, mainstadium[4].toString());

            int id = (int) database.insertOrThrow(BbchMainStadium.TABLE_NAME, null, values);

            for(String[] stadium: (String[][]) mainstadium[5]){
                values = new ContentValues();

                values.put(BbchStadium.COLUMN_MAIN_ID, "" + id);
                values.put(BbchStadium.COLUMN_NUMBER, stadium[0]);
                values.put(BbchStadium.COLUMN_NAME_EN, stadium[1]);
                values.put(BbchStadium.COLUMN_NAME_DE, stadium[2]);

                database.insertOrThrow(BbchStadium.TABLE_NAME, null, values);
            }

        }
    }
}
