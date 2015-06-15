package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;

/**
 * Created by Toni on 10.05.2015.
 */
public class Marker extends DbModelInterface {

    public int id               = -1;
    public int versuchId        = -1;
    public String code          = null;
    public String name          = null;
    public String beschreibung  = null;
    public String foto          = null;
    public int type             = -1;

    public static final int MARKER_TYPE_BONITUR        = 1;
    public static final int MARKER_TYPE_MESSEN         = 2;
    public static final int MARKER_TYPE_DATUM          = 3;
    public static final int MARKER_TYPE_BEMERKUNG      = 4;

    public static String COLUMN_ID              = "_id";
    public static String COLUMN_VERSUCH         = "versuchId";
    public static String COLUMN_CODE            = "code";
    public static String COLUMN_NAME            = "name";
    public static String COLUMN_BESCHREIBUNG    = "beschreibung";
    public static String COLUMN_FOTO            = "foto";
    public static String COLUMN_TYPE            = "type";

    public static String TABLE_NAME = "marker";

    public static String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "(" + '\n'+
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            COLUMN_VERSUCH      + " INTEGER NOT NULL," + '\n' +
            COLUMN_CODE         + " TEXT NOT NULL," + '\n' +
            COLUMN_NAME         + " TEXT NOT NULL," + '\n' +
            COLUMN_BESCHREIBUNG + " TEXT," + '\n' +
            COLUMN_FOTO         + " TEXT," + '\n' +
            COLUMN_TYPE         + " INTEGER NOT NULL," + '\n' +

            "CONSTRAINT marker_versuch FOREIGN KEY("+COLUMN_VERSUCH+") REFERENCES "+ Versuch.TABLE_NAME + "("+Versuch.COLUMN_ID+")"+ '\n'+
                "ON UPDATE CASCADE " + '\n' +
                "ON DELETE CASCADE " + '\n' +
    ")";

    @Override
    boolean save() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_VERSUCH, versuchId);
        values.put(COLUMN_CODE, code);
        values.put(COLUMN_BESCHREIBUNG, beschreibung);
        values.put(COLUMN_FOTO, foto);
        values.put(COLUMN_TYPE, type == -1 ? null: type);

        id = saveRow(id,values);
        return id==-1;
    }
}
