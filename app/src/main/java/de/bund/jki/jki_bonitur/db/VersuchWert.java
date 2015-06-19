package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 11.05.2015.
 */
public class VersuchWert extends DbModelInterface {

    public int id               = -1;
    public int markerId         = -1;
    public int versuchId        = -1;
    public int standortId       = -1;
    public int wert_int         = -1;
    public String wert_datum    = null;
    public String wert_text     = null;
    public int wert_id          = -1;

    public MarkerWert markerWert = null;


    public static String COLUMN_ID          = "_id";
    public static String COLUMN_MARKER      = "markerId";
    public static String COLUMN_VERSUCH     = "versuchId";
    public static String COLUMN_STANDORT    = "standortId";
    public static String COLUMN_WERT_INT    = "wert_int";
    public static String COLUMN_WERT_DATUM  = "wert_datum";
    public static String COLUMN_WERT_TEXT   = "wert_text";
    public static String COLUMN_WERT_ID     = "wert_id";

    public static String TABLE_NAME = "versuchWert";

    public static String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "(" + '\n'+
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            COLUMN_MARKER       + " INTEGER NOT NULL," + '\n' +
            COLUMN_VERSUCH      + " INTEGER NOT NULL," + '\n' +
            COLUMN_STANDORT     + " INTEGER NOT NULL," + '\n' +
            COLUMN_WERT_INT     + " TEXT," + '\n' +
            COLUMN_WERT_DATUM   + " TEXT," + '\n' +
            COLUMN_WERT_TEXT    + " TEXT," + '\n' +
            COLUMN_WERT_ID      + " INTEGER," + '\n' +

            "CONSTRAINT versuchWert_versuch FOREIGN KEY("+COLUMN_VERSUCH+") REFERENCES "+ Marker.TABLE_NAME + "("+Marker.COLUMN_VERSUCH+")"+ '\n'+
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +

            "CONSTRAINT versuchWert_marker FOREIGN KEY("+COLUMN_MARKER+") REFERENCES "+ Marker.TABLE_NAME + "("+Marker.COLUMN_ID+")"+ '\n'+
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +

            "CONSTRAINT versuchWert_standort FOREIGN KEY("+COLUMN_STANDORT+") REFERENCES "+ Standort.TABLE_NAME + "("+Standort.COLUMN_ID+")"+ '\n'+
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +

            "CONSTRAINT versuchWert_markerWert FOREIGN KEY("+COLUMN_WERT_ID+") REFERENCES "+ MarkerWert.TABLE_NAME + "("+MarkerWert.COLUMN_ID+")"+ '\n'+
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +
    ")";

    @Override
    boolean save() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_VERSUCH,      versuchId);
        values.put(COLUMN_MARKER,       markerId);
        values.put(COLUMN_STANDORT,     standortId);
        values.put(COLUMN_WERT_INT,     wert_int    != -1 ? wert_int    : null);
        values.put(COLUMN_WERT_DATUM,   wert_datum);
        values.put(COLUMN_WERT_TEXT,    wert_text);
        values.put(COLUMN_WERT_ID,      wert_id     != -1 ? wert_id     : null);

        id = saveRow(id,values);
        return id==-1;
    }

    public static VersuchWert findByPk(int id){
        VersuchWert res = new VersuchWert();

        Cursor c = BoniturSafe.db.query(
                VersuchWert.TABLE_NAME,
                new String[]{VersuchWert.COLUMN_ID, VersuchWert.COLUMN_VERSUCH, VersuchWert.COLUMN_WERT_INT, VersuchWert.COLUMN_WERT_TEXT, VersuchWert.COLUMN_WERT_ID},
                VersuchWert.COLUMN_ID+"=?",
                new String [] {""+id},
                null,
                null,
                null);

        if(c.getCount() == 1){
            c.moveToFirst();
            res.id =        c.getInt(c.getColumnIndex(VersuchWert.COLUMN_ID));
            res.versuchId   = c.getInt(c.getColumnIndex(VersuchWert.COLUMN_VERSUCH));
            res.wert_id     = c.getInt(c.getColumnIndex(VersuchWert.COLUMN_WERT_ID));
            res.wert_int    = c.getInt(c.getColumnIndex(VersuchWert.COLUMN_WERT_INT));
            res.wert_text   = c.getString(c.getColumnIndex(VersuchWert.COLUMN_WERT_TEXT));

            return res;
        }

        return null;
    }
}
