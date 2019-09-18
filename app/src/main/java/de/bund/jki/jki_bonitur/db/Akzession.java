package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public class Akzession extends DbModelInterface {

    public static String TABLE_NAME      = "akzession";
    public static String COLUMN_ID       = "_id";
    public static String COLUMN_VERSUCH  = "versuchId";
    public static String COLUMN_PASSPORT = "passportId";
    public static String COLUMN_NUMMER   = "nummer";
    public static String COLUMN_NAME     = "name";
    public static String COLUMN_MERKMALE = "merkmale";
    public static String CREATE_TABLE    =
            "CREATE TABLE " + TABLE_NAME + "(" + '\n' +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + '\n' +
                    COLUMN_VERSUCH + " INTEGER NOT NULL," + '\n' +
            COLUMN_PASSPORT + " INTEGER," + '\n' +
                    COLUMN_NUMMER + " TEXT," + '\n' +
                    COLUMN_NAME + " TEXT," + '\n' +
            COLUMN_MERKMALE + " TEXT," + '\n' +

                    "CONSTRAINT akzession_standort FOREIGN KEY(" + COLUMN_VERSUCH + ") REFERENCES " + Standort.TABLE_NAME + "(" + Standort.COLUMN_VERSUCH + ")" + '\n' +
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +

                    "CONSTRAINT akzession_passport FOREIGN KEY(" + COLUMN_PASSPORT + ") REFERENCES " + Passport.TABLE_NAME + "(" + Passport.COLUMN_ID + ")" + '\n' +

                    ")";
    public        int    id              = - 1;
    public        int    versuchId       = - 1;
    public        int    passportId      = - 1;
    public        String nummer          = null;
    public        String name            = null;
    public        String merkmale        = null;

    public Akzession() {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    public static Akzession findByPk(int id) {
        Akzession res = new Akzession();

        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                    Akzession.TABLE_NAME,
                    new String[]{Akzession.COLUMN_ID, Akzession.COLUMN_VERSUCH, Akzession.COLUMN_PASSPORT, Akzession.COLUMN_NUMMER, Akzession.COLUMN_NAME, Akzession.COLUMN_MERKMALE},
                    Akzession.COLUMN_ID + "=?",
                    new String[]{"" + id},
                    null,
                    null,
                    null
            );

            if (c.getCount() == 1) {
                c.moveToFirst();
                res.id = c.getInt(c.getColumnIndex(Akzession.COLUMN_ID));
                res.versuchId = c.getInt(c.getColumnIndex(Akzession.COLUMN_VERSUCH));
                res.passportId = c.getInt(c.getColumnIndex(Akzession.COLUMN_PASSPORT));
                res.nummer = c.getString(c.getColumnIndex(Akzession.COLUMN_NUMMER));
                res.name = c.getString(c.getColumnIndex(Akzession.COLUMN_NAME));
                res.merkmale = c.getString(c.getColumnIndex(Akzession.COLUMN_MERKMALE));

                //c.close();

                return res;
            }
            //c.close();
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
        values.put(COLUMN_PASSPORT, passportId != - 1 ? passportId : null);
        values.put(COLUMN_NUMMER, nummer);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MERKMALE, merkmale);

        id = saveRow(id, values);
        return id == - 1;
    }


}
