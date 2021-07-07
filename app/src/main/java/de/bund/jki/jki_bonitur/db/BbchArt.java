package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public class BbchArt extends DbModelInterface {

    public static String TABLE_NAME     = "bbch_art";

    public static String COLUMN_ID      = "_id";
    public static String COLUMN_NAME_EN = "name_en";
    public static String COLUMN_NAME_DE = "name_de";
    public static String CREATE_TABLE   = "CREATE TABLE " + BbchArt.TABLE_NAME + "(" + '\n' +
            BbchArt.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + '\n' +
            BbchArt.COLUMN_NAME_EN + " VARCHAR(100)," + '\n' +
            BbchArt.COLUMN_NAME_DE + " VARCHAR(100)" + '\n' +
            ")";

    public int      id      = - 1;
    public String   name_en = null;
    public String   name_de = null;
    public String[] names   = null;

    public BbchArt() {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    public static BbchArt findByPk(int id) {
        BbchArt res = new BbchArt();

        Cursor c = null;
        try {
            c = BoniturSafe.db.rawQuery(
                    "SELECT * FROM " + BbchArt.TABLE_NAME + " WHERE " + BbchArt.COLUMN_ID + " = ?",
                    new String[]{"" + id}
            );

            if (c.getCount() == 1) {
                c.moveToFirst();
                res.id = c.getInt(c.getColumnIndex(BbchArt.COLUMN_ID));
                res.name_en = c.getString(c.getColumnIndex(BbchArt.COLUMN_NAME_EN));
                res.name_de = c.getString(c.getColumnIndex(BbchArt.COLUMN_NAME_DE));
                res.names = new String[]{res.name_en, res.name_de};


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
        values.put(COLUMN_NAME_DE, this.name_de);
        values.put(COLUMN_NAME_EN, this.name_en);
        id = saveRow(id, values);
        return id == - 1;
    }

    public static void insertStartValues(SQLiteDatabase database) {
        String[][] daten = new String[][]{
                {"1", "Cereals", "Getreide"},
                {"2", "Rape", "Raps"},
                {"3", "Rice", "Reis"},
                {"4", "Maize", "Mais"},
                {"5", "Grapevine", "Weinrebe"}
        };

        for (String[] art : daten) {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID, art[0]);
            values.put(COLUMN_NAME_EN, art[1]);
            values.put(COLUMN_NAME_DE, art[2]);

            database.insertOrThrow(TABLE_NAME, null, values);
        }
    }

    public static void insertMoreValues(SQLiteDatabase database) {
        String[][] daten = new String[][]{
            {"6", "Apple", "Apfel"},
            {"7", "Patato", "Kartoffel"}
        };

        for (String[] art : daten) {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID, art[0]);
            values.put(COLUMN_NAME_EN, art[1]);
            values.put(COLUMN_NAME_DE, art[2]);

            database.insertOrThrow(TABLE_NAME, null, values);
        }
    }

}
