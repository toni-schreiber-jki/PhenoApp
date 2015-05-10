package de.bund.jki.jki_bonitur.db;

/**
 * Created by Toni on 10.05.2015.
 */
public class Marker extends DbModelInterface {

    public static final int MARKER_TYPE_BONITUR        = 1;
    public static final int MARKER_TYPE_MESSEN         = 2;
    public static final int MARKER_TYPE_DATUM          = 3;
    public static final int MARKER_TYPE_BEMERKUNG      = 4;

    public static String COLUMN_ID              = "_id";
    public static String COLUMN_VERSUCH         = "versuchId";
    public static String COLUMN_CODE            = "code";
    public static String COLUMN_BESCHREIBUNG    = "beschreibung";
    public static String COLUMN_FOTO            = "foto";
    public static String COLUMN_TYPE            = "type";

    public static String TABLE_NAME = "marker";

    public static String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "(" + '\n'+
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            COLUMN_VERSUCH      + " INTEGER NOT NULL," + '\n' +
            COLUMN_CODE         + " TEXT NOT NULL," + '\n' +
            COLUMN_BESCHREIBUNG + " INTEGER," + '\n' +
            COLUMN_FOTO         + " INTEGER," + '\n' +
            COLUMN_TYPE         + " INTEGER NOT NULL," + '\n' +

            "CONSTRAINT marker_versuch FOREIGN KEY("+COLUMN_VERSUCH+") REFERENCES "+ Versuch.TABLE_NAME + "("+Versuch.COLUMN_ID+")"+ '\n'+
                "ON UPDATE CASCADE " + '\n' +
                "ON DELETE CASCADE " + '\n' +
    ")";
}
