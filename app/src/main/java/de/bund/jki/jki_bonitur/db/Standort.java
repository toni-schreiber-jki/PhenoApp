package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;

/**
 * Created by Toni on 10.05.2015.
 */
public class Standort extends DbModelInterface {

    public int id           = -1;
    public int versuchId    = -1;
    public String parzelle  = null;
    public int reihe        = -1;
    public int pflanze      = -1;
    public int akzessionId  = -1;
    public int passportId   = -1;

    public static String COLUMN_ID              = "_id";
    public static String COLUMN_VERSUCH         = "versuchId";
    public static String COLUMN_PARZELLE        = "parzelle";
    public static String COLUMN_REIHE           = "reihe";
    public static String COLUMN_PFLANZE         = "pflanze";
    public static String COLUMN_AKZESSION       = "akzessionId";
    public static String COLUMN_PASSPORT        = "passportId";

    public static String TABLE_NAME = "standort";

    public static String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "(" + '\n'+
            COLUMN_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            COLUMN_VERSUCH  + " INTEGER NOT NULL," + '\n' +
            COLUMN_PARZELLE + " TEXT NOT NULL DEFAULT '00'," + '\n' +
            COLUMN_REIHE    + " INTEGER NOT NULL," + '\n' +
            COLUMN_PFLANZE  + " INTEGER NOT NULL," + '\n' +
            //COLUMN_AKZESSION+ " INTEGER," + '\n' +
            //COLUMN_PASSPORT + " INTEGER," + '\n' +

            "CONSTRAINT standort_versuch FOREIGN KEY("+COLUMN_VERSUCH+") REFERENCES "+ Versuch.TABLE_NAME + "("+Versuch.COLUMN_ID+")"+ '\n'+
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +
    ")";
    public static String ALTER_TABLE_1 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN "+ COLUMN_AKZESSION+" INTEGER REFERENCES "+ Akzession.TABLE_NAME + "("+Versuch.COLUMN_ID+")"+ '\n';
    public static String ALTER_TABLE_2 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN  "+COLUMN_PASSPORT+" INTEGER REFERENCES "+ Passport.TABLE_NAME + "("+Versuch.COLUMN_ID+")"+ '\n';

    @Override
    boolean save() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_VERSUCH,      versuchId);
        values.put(COLUMN_PARZELLE,     parzelle);
        values.put(COLUMN_REIHE,        reihe);
        values.put(COLUMN_PFLANZE,      pflanze);
        values.put(COLUMN_AKZESSION,    akzessionId     != -1 ? akzessionId: null);
        values.put(COLUMN_PASSPORT,     passportId      != -1 ? passportId : null);

        return saveRow(id,values);
    }

}
