package de.bund.jki.jki_bonitur.db;

/**
 * Created by Toni on 11.05.2015.
 */
public class Passport extends DbModelInterface {

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
}
