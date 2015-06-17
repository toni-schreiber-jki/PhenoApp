package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.SQLException;

import org.apache.poi.ss.formula.ptg.TblPtg;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public class Versuch extends DbModelInterface {

    public int id       = -1;
    public String name  = null;

    public static String COLUMN_ID = "_id";
    public static String COLUMN_NAME = "name";

    public static String TABLE_NAME = "versuch";

    public static String CREATE_TABLE = "CREATE TABLE "+ Versuch.TABLE_NAME + "(" + '\n'+
            Versuch.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            Versuch.COLUMN_NAME + " VARCHAR(100) NOT NULL" + '\n' +
    ")";

    public Versuch()
    {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    @Override
    public boolean save() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);

        id = saveRow(id,values);
        return id==-1;
    }
}
