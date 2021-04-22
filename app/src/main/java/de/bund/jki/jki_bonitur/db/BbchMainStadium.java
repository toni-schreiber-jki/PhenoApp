package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public class BbchMainStadium extends DbModelInterface {

    public static String TABLE_NAME = "bbch_main_stadium";

    public static String COLUMN_ID      = "_id";
    public static String COLUMN_ART_ID  = "art_id";
    public static String COLUMN_NUMBER  = "number";
    public static String COLUMN_NAME_EN = "name_en";
    public static String COLUMN_NAME_DE = "name_de";
    public static String COLUMN_IMAGE   = "image_file_name";

    public static String CREATE_TABLE = "CREATE TABLE " + BbchMainStadium.TABLE_NAME + "(" + '\n' +
            BbchMainStadium.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + '\n' +
            BbchMainStadium.COLUMN_ART_ID + " INTEGER NOT NULL," + '\n' +
            BbchMainStadium.COLUMN_NUMBER + " TINYINT NOT NULL," + '\n' +
            BbchMainStadium.COLUMN_NAME_EN + " VARCHAR(255)," + '\n' +
            BbchMainStadium.COLUMN_NAME_DE + " VARCHAR(255)," + '\n' +
            BbchMainStadium.COLUMN_IMAGE + " VARCHAR(255)," + '\n' +

            "CONSTRAINT fk_main_stadium_art FOREIGN KEY(" + COLUMN_ART_ID + ") " +
            "REFERENCES " + BbchArt.TABLE_NAME + "(" + BbchArt.COLUMN_ID + ")" + '\n' +
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +

            ")";

    public int      id              = - 1;
    public int      art_id          = - 1;
    public int      number          = - 1;
    public BbchArt  art             = null;
    public String   name_en         = null;
    public String   name_de         = null;
    public String[] names           = null;
    public String   image_file_name = null;

    public BbchMainStadium() {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    public static BbchMainStadium findByPk(int id) {
        BbchMainStadium res = new BbchMainStadium();

        Cursor c = null;
        try {
            c = BoniturSafe.db.rawQuery(
                    "SELECT * FROM " + BbchMainStadium.TABLE_NAME + " WHERE " + BbchMainStadium.COLUMN_ID + " = ?",
                    new String[]{"" + id}
            );

            if (c.getCount() == 1) {
                c.moveToFirst();
                res.id = c.getInt(c.getColumnIndex(COLUMN_ID));
                res.art_id = c.getInt(c.getColumnIndex(COLUMN_ART_ID));
                res.number = c.getInt(c.getColumnIndex(COLUMN_NUMBER));
                res.name_en = c.getString(c.getColumnIndex(COLUMN_NAME_EN));
                res.name_de = c.getString(c.getColumnIndex(COLUMN_NAME_DE));
                res.image_file_name = c.getString(c.getColumnIndex(COLUMN_IMAGE));

                res.names = new String[]{res.name_en, res.name_de};
                if (res.art_id != - 1) {
                    res.art = BbchArt.findByPk(res.art_id);
                }


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

        values.put(COLUMN_ART_ID, this.art_id);
        values.put(COLUMN_NUMBER, this.number);
        values.put(COLUMN_NAME_DE, this.name_de);
        values.put(COLUMN_NAME_EN, this.name_en);
        values.put(COLUMN_IMAGE, this.image_file_name);

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
}
