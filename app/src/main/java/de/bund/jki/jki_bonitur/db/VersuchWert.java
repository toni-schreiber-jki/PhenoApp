package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 11.05.2015.
 */
public class VersuchWert extends DbModelInterface {

    public static String COLUMN_ID               = "_id";
    public static String COLUMN_MARKER           = "markerId";
    public static String COLUMN_VERSUCH          = "versuchId";
    public static String COLUMN_STANDORT         = "standortId";
    public static String COLUMN_WERT_INT         = "wert_int";
    public static String COLUMN_WERT_DATUM       = "wert_datum";
    public static String COLUMN_WERT_TEXT        = "wert_text";
    public static String COLUMN_WERT_ID          = "wert_id";
    public static String COLUMN_WERT_NUMERIC     = "wert_numeric";
    public static String TABLE_NAME              = "versuchWert";
    public static String CREATE_TABLE            = "CREATE TABLE " + TABLE_NAME + "(" + '\n' +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + '\n' +
        COLUMN_MARKER + " INTEGER NOT NULL," + '\n' +
        COLUMN_VERSUCH + " INTEGER NOT NULL," + '\n' +
        COLUMN_STANDORT + " INTEGER NOT NULL," + '\n' +
        COLUMN_WERT_INT + " TEXT," + '\n' +
        COLUMN_WERT_DATUM + " TEXT," + '\n' +
        COLUMN_WERT_TEXT + " TEXT," + '\n' +
        COLUMN_WERT_ID + " INTEGER," + '\n' +

        "CONSTRAINT versuchWert_versuch FOREIGN KEY(" + COLUMN_VERSUCH + ") REFERENCES " + Marker.TABLE_NAME + "(" + Marker.COLUMN_VERSUCH + ")" + '\n' +
        "ON UPDATE CASCADE " + '\n' +
        "ON DELETE CASCADE " + '\n' +

        "CONSTRAINT versuchWert_marker FOREIGN KEY(" + COLUMN_MARKER + ") REFERENCES " + Marker.TABLE_NAME + "(" + Marker.COLUMN_ID + ")" + '\n' +
        "ON UPDATE CASCADE " + '\n' +
        "ON DELETE CASCADE " + '\n' +

        "CONSTRAINT versuchWert_standort FOREIGN KEY(" + COLUMN_STANDORT + ") REFERENCES " + Standort.TABLE_NAME + "(" + Standort.COLUMN_ID + ")" + '\n' +
        "ON UPDATE CASCADE " + '\n' +
        "ON DELETE CASCADE " + '\n' +

        "CONSTRAINT versuchWert_markerWert FOREIGN KEY(" + COLUMN_WERT_ID + ") REFERENCES " + MarkerWert.TABLE_NAME + "(" + MarkerWert.COLUMN_ID + ")" + '\n' +
        "ON UPDATE CASCADE " + '\n' +
        "ON DELETE CASCADE " + '\n' +
        ")";
    public static String ALTER_TABLE_ADD_NUMERIC = "ALTER TABLE " + TABLE_NAME + " " +
        "ADD " + COLUMN_WERT_NUMERIC + " NUMERIC(10,3)";

    public int        id              = - 1;
    public int        markerId        = - 1;
    public int        versuchId       = - 1;
    public int        standortId      = - 1;
    public int        wert_int        = - 1;
    public boolean    int_is_null     = false;
    public String     wert_datum      = null;
    public String     wert_text       = null;
    public int        wert_id         = - 1;
    public boolean    numeric_is_null = false;
    public double     wert_numeric    = - 1;
    public MarkerWert markerWert      = null;

    public VersuchWert() {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    public static VersuchWert findByPk(int id) {
        VersuchWert res = new VersuchWert();

        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                VersuchWert.TABLE_NAME,
                new String[]{
                    VersuchWert.COLUMN_ID,
                    VersuchWert.COLUMN_VERSUCH,
                    VersuchWert.COLUMN_WERT_INT,
                    VersuchWert.COLUMN_WERT_TEXT,
                    VersuchWert.COLUMN_WERT_ID,
                    VersuchWert.COLUMN_WERT_NUMERIC
                },
                VersuchWert.COLUMN_ID + "=?",
                new String[]{"" + id},
                null,
                null,
                null
            );

            if (c.getCount() == 1) {
                c.moveToFirst();

                res.fillWithCursor(c);

                return res;
            }

            return null;
        } finally {
            if (c != null)
                c.close();
        }
    }

    @Override
    public boolean save() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_VERSUCH, versuchId);
        values.put(COLUMN_MARKER, markerId);
        values.put(COLUMN_STANDORT, standortId);
        if (wert_int == - 1) {
            values.putNull(COLUMN_WERT_INT);
        } else {
            values.put(COLUMN_WERT_INT, wert_int);
        }
        values.put(COLUMN_WERT_DATUM, wert_datum);
        values.put(COLUMN_WERT_TEXT, wert_text);
        values.put(COLUMN_WERT_ID, wert_id != - 1 ? wert_id : null);
        if (wert_numeric == - 1) {
            values.putNull(COLUMN_WERT_NUMERIC);
        } else {
            values.put(COLUMN_WERT_NUMERIC, wert_numeric);
        }

        id = saveRow(id, values);
        return id == - 1;
    }

    public void fillWithCursor(Cursor c) {
        this.id = c.getInt(c.getColumnIndex(VersuchWert.COLUMN_ID));
        this.versuchId = c.getInt(c.getColumnIndex(VersuchWert.COLUMN_VERSUCH));
        this.wert_id = c.getInt(c.getColumnIndex(VersuchWert.COLUMN_WERT_ID));
        this.int_is_null = c.isNull(c.getColumnIndex(VersuchWert.COLUMN_WERT_INT));
        this.wert_int = c.getInt(c.getColumnIndex(VersuchWert.COLUMN_WERT_INT));
        this.wert_text = c.getString(c.getColumnIndex(VersuchWert.COLUMN_WERT_TEXT));
        this.numeric_is_null = c.isNull(c.getColumnIndex(VersuchWert.COLUMN_WERT_NUMERIC));
        this.wert_numeric = c.getDouble(c.getColumnIndex(VersuchWert.COLUMN_WERT_NUMERIC));
    }
}
