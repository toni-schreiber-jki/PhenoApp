package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 11.05.2015.
 */
public class MarkerWert extends DbModelInterface {

    public static String COLUMN_ID     = "_id";
    public static String COLUMN_MARKER = "markerId";
    public static String COLUMN_LABEL  = "label";
    public static String COLUMN_VALUE  = "value";
    public static String TABLE_NAME    = "markerWert";
    public static String CREATE_TABLE  = "CREATE TABLE " + TABLE_NAME + "(" + '\n' +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + '\n' +
            COLUMN_MARKER + " INTEGER NOT NULL," + '\n' +
            COLUMN_LABEL + " TEXT," + '\n' +
            COLUMN_VALUE + " INTEGER NOT NULL," + '\n' +

            "CONSTRAINT markerWert_marker FOREIGN KEY(" + COLUMN_MARKER + ") REFERENCES " + Marker.TABLE_NAME + "(" + Marker.COLUMN_ID + ")" + '\n' +
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +
            ")";

    public int    id       = - 1;
    public int    markerId = - 1;
    public String label    = null;
    public String value    = null;

    public static MarkerWert findByPk(int id) {
        MarkerWert res = new MarkerWert();

        Cursor c = null;
        try {
            c = BoniturSafe.db.rawQuery(
                    "SELECT * FROM " + TABLE_NAME + " WHERE " + MarkerWert.COLUMN_ID + " = ?",
                    new String[]{"" + id}
            );

            if (c.getCount() == 1) {
                c.moveToFirst();
                res.id = id;
                res.markerId = c.getInt(c.getColumnIndex(MarkerWert.COLUMN_MARKER));
                res.label = c.getString(c.getColumnIndex(MarkerWert.COLUMN_LABEL));
                res.value = c.getString(c.getColumnIndex(MarkerWert.COLUMN_VALUE));
            }

            //c.close();

            return res;
        } finally {
            c.close();
        }
    }

    @Override
    boolean save() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MARKER, markerId);
        values.put(COLUMN_LABEL, label);
        values.put(COLUMN_VALUE, value);

        id = saveRow(id, values);
        return id == - 1;
    }
}
