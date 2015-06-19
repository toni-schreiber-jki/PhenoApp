package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 11.05.2015.
 */
public class Passport extends DbModelInterface {

    public int id           = -1;
    public int versuchId    = -1;
    public String leitname  = null;
    public String kennNr    = null;
    public String merkmale  = null;

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

    public Passport(){
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    public static Passport findByPk(int id){
        Passport res = new Passport();

        Cursor c = BoniturSafe.db.query(
                Passport.TABLE_NAME,
                new String[]{Passport.COLUMN_ID, Passport.COLUMN_VERSUCH, Passport.COLUMN_LEITNAME, Passport.COLUMN_KENN_NR, Passport.COLUMN_MERKMALE},
                Passport.COLUMN_ID+"=?",
                new String [] {""+id},
                null,
                null,
                null);

        if(c.getCount() == 1){
            c.moveToFirst();
            res.id =        c.getInt(c.getColumnIndex(Passport.COLUMN_ID));
            res.versuchId = c.getInt(c.getColumnIndex(Passport.COLUMN_VERSUCH));
            res.kennNr =    c.getString(c.getColumnIndex(Passport.COLUMN_KENN_NR));
            res.leitname =      c.getString(c.getColumnIndex(Passport.COLUMN_LEITNAME));
            res.merkmale =  c.getString(c.getColumnIndex(Passport.COLUMN_MERKMALE));

            return res;
        }

        return null;
    }

    @Override
    public boolean save() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_VERSUCH, versuchId);
        values.put(COLUMN_LEITNAME, leitname);
        values.put(COLUMN_KENN_NR, kennNr);
        values.put(COLUMN_MERKMALE, merkmale);

        id = saveRow(id,values);
        return id==-1;
    }
}
