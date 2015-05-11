package de.bund.jki.jki_bonitur.db;

/**
 * Created by Toni on 10.05.2015.
 */
public class Akzession extends DbModelInterface {

    public static String TABLE_NAME ="akzession";

    public static String COLUMN_ID          ="_id";
    public static String COLUMN_VERSUCH     ="versuchId";
    public static String COLUMN_PASSPORT    ="passportId";
    public static String COLUMN_NUMMER      ="nummer";
    public static String COLUMN_NAME        ="name";
    public static String COLUMN_MERKMALE    ="merkmale";

    public static String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "(" + '\n'+
            COLUMN_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            COLUMN_VERSUCH  + " INTEGER NOT NULL," + '\n' +
            COLUMN_PASSPORT + " INTEGER," + '\n' +
            COLUMN_NUMMER   + " TEXT," + '\n' +
            COLUMN_NAME     + " TEXT," + '\n' +
            COLUMN_MERKMALE + " TEXT," + '\n' +

            "CONSTRAINT akzession_standort FOREIGN KEY("+COLUMN_VERSUCH+") REFERENCES "+ Standort.TABLE_NAME + "("+Standort.COLUMN_VERSUCH+")"+ '\n'+
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +

            "CONSTRAINT akzession_passport FOREIGN KEY("+COLUMN_PASSPORT+") REFERENCES "+ Passport.TABLE_NAME + "("+Passport.COLUMN_ID+")"+ '\n'+

    ")";


}
