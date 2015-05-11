package de.bund.jki.jki_bonitur.db;

/**
 * Created by Toni on 10.05.2015.
 */
public class Standort extends DbModelInterface {

    public static String COLUMN_ID              = "_id";
    public static String COLUMN_VERSUCH         = "name";
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
}
