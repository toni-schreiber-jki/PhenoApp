package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;

/**
 * Created by Toni on 11.05.2015.
 */
public class Passport extends DbModelInterface {

    public int id           = -1;
    public int versuchId    = -1;
    public String leitname  = null;
    public String kennNr    = null;
    public String merkmale  = null;

    public static String COLUMN_ID          = "_id";
    public static String COLUMN_VERSUCH     = "versuchId";
    public static String COLUMN_LEITNAME    = "leitname";
    public static String COLUMN_KENN_NR     = "kennNr";
    public static String COLUMN_MERKMALE    = "merkmale";

    public static String TABLE_NAME = "passport";

    public static String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "(" + '\n'+
            COLUMN_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            COLUMN_VERSUCH + " INTEGER NOT NULL," + '\n' +
            COLUMN_LEITNAME + " TEXT," + '\n' +
            COLUMN_KENN_NR  + " TEXT," + '\n' +
            COLUMN_MERKMALE + " TEXT," + '\n' +

            "CONSTRAINT passport_standort FOREIGN KEY("+ COLUMN_VERSUCH +") REFERENCES "+ Standort.TABLE_NAME + "("+Standort.COLUMN_VERSUCH+")"+ '\n'+
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +
    ")";

    public Passport()
    {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    @Override
    public boolean save() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_VERSUCH, versuchId);
        values.put(COLUMN_LEITNAME, leitname);
        values.put(COLUMN_KENN_NR, kennNr);
        values.put(COLUMN_MERKMALE, merkmale);

        id = saveRow(id,values);
        return id==-1;
    }
}
