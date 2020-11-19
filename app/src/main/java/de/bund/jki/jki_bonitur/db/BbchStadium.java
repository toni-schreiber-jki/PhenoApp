package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public class BbchStadium extends DbModelInterface {

    public static String TABLE_NAME = "bbch_stadium";

    public static String COLUMN_ID      = "_id";
    public static String COLUMN_MAIN_ID = "main_id";
    public static String COLUMN_NUMBER  = "number";
    public static String COLUMN_NAME_EN = "name_en";
    public static String COLUMN_NAME_DE = "name_de";

    public static String CREATE_TABLE = "CREATE TABLE " + BbchStadium.TABLE_NAME + "(" + '\n' +
        BbchStadium.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + '\n' +
        BbchStadium.COLUMN_MAIN_ID + " INTEGER NOT NULL," + '\n' +
        BbchStadium.COLUMN_NUMBER + " TINYINT NOT NULL," + '\n' +
        BbchStadium.COLUMN_NAME_EN + " VARCHAR(255)," + '\n' +
        BbchStadium.COLUMN_NAME_DE + " VARCHAR(255)," + '\n' +

        "CONSTRAINT fk_stadium_main_stadium FOREIGN KEY(" + COLUMN_MAIN_ID + ") " +
        "REFERENCES " + BbchMainStadium.TABLE_NAME + "(" + BbchMainStadium.COLUMN_ID + ")" + '\n' +
        "ON UPDATE CASCADE " + '\n' +
        "ON DELETE CASCADE " + '\n' +

        ")";

    public int             id              = - 1;
    public int             main_id         = - 1;
    public BbchMainStadium mainStadium     = null;
    public int             number          = - 1;
    public String          name_en         = null;
    public String          name_de         = null;
    public String[]        names           = null;

    public BbchStadium() {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    public static BbchStadium findByPk(int id) {
        BbchStadium res = new BbchStadium();

        Cursor c = null;
        try {
            c = BoniturSafe.db.rawQuery(
                "SELECT * FROM " + BbchStadium.TABLE_NAME + " WHERE " + BbchStadium.COLUMN_ID + " = ?",
                new String[]{"" + id}
            );

            if (c.getCount() == 1) {
                c.moveToFirst();
                res.id = c.getInt(c.getColumnIndex(COLUMN_ID));
                res.main_id = c.getInt(c.getColumnIndex(COLUMN_MAIN_ID));
                res.number = c.getInt(c.getColumnIndex(COLUMN_NUMBER));
                res.name_en = c.getString(c.getColumnIndex(COLUMN_NAME_EN));
                res.name_de = c.getString(c.getColumnIndex(COLUMN_NAME_DE));

                res.names = new String[]{res.name_en, res.name_de};
                if (res.main_id != - 1) {
                    res.mainStadium = BbchMainStadium.findByPk(res.main_id);
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

        values.put(COLUMN_MAIN_ID, this.main_id);
        values.put(COLUMN_NUMBER, this.number);
        values.put(COLUMN_NAME_DE, this.name_de);
        values.put(COLUMN_NAME_EN, this.name_en);

        id = saveRow(id, values);
        return id == - 1;
    }
}
