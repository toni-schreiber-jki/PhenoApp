package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public class Standort extends DbModelInterface {

    public int id           = -1;
    public int versuchId    = -1;
    public String parzelle  = null;
    public int reihe        = -1;
    public int pflanze      = -1;
    public int akzessionId  = -1;
    public int passportId   = -1;
    public String sorte;
    public String zuchtstamm;
    public String mutter;
    public String vater;
    public String sortimentsnummer;
    public String freifeld;

    public static String COLUMN_ID              = "_id";
    public static String COLUMN_VERSUCH         = "versuchId";
    public static String COLUMN_PARZELLE        = "parzelle";
    public static String COLUMN_REIHE           = "reihe";
    public static String COLUMN_PFLANZE         = "pflanze";
    public static String COLUMN_AKZESSION       = "akzessionId";
    public static String COLUMN_PASSPORT        = "passportId";
    public static String COLUMN_SORTE           = "sorte";
    public static String COLUMN_ZUCHTSTAMM      = "zuchtstamm";
    public static String COLUMN_MUTTER          = "Mutter";
    public static String COLUMN_VATER           = "Vater";
    public static String COLUMN_SORTIMENTNR     = "Sortimentsnummer";
    public static String COLUMN_FREIFELD        = "freifeld";

    public static String TABLE_NAME = "standort";

    public static String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "(" + '\n'+
            COLUMN_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            COLUMN_VERSUCH      + " INTEGER NOT NULL," + '\n' +
            COLUMN_PARZELLE     + " TEXT NOT NULL DEFAULT '00'," + '\n' +
            COLUMN_REIHE        + " INTEGER NOT NULL," + '\n' +
            COLUMN_PFLANZE      + " INTEGER NOT NULL," + '\n' +
            COLUMN_SORTE        + " TEXT," + '\n' +
            COLUMN_ZUCHTSTAMM   + " TEXT," + '\n' +
            COLUMN_MUTTER       + " TEXT," + '\n' +
            COLUMN_VATER        + " TEXT," + '\n' +
            COLUMN_SORTIMENTNR  + " TEXT,"+ '\n' +
            //COLUMN_AKZESSION+ " INTEGER," + '\n' +
            //COLUMN_PASSPORT + " INTEGER," + '\n' +

            "CONSTRAINT standort_versuch FOREIGN KEY("+COLUMN_VERSUCH+") REFERENCES "+ Versuch.TABLE_NAME + "("+Versuch.COLUMN_ID+")"+ '\n'+
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +
    ")";
    public static String ALTER_TABLE_1 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_AKZESSION+" INTEGER REFERENCES "+ Akzession.TABLE_NAME + "("+Versuch.COLUMN_ID+")"+ '\n';
    public static String ALTER_TABLE_2 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PASSPORT +" INTEGER REFERENCES "+ Passport.TABLE_NAME + "("+Versuch.COLUMN_ID+")"+ '\n';
    public static String ALTER_TABLE_3 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_FREIFELD +" TEXT" +'\n';


    public Standort()
    {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    @Override
    public boolean save() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_VERSUCH,      versuchId);
        values.put(COLUMN_PARZELLE,     parzelle);
        values.put(COLUMN_REIHE,        reihe);
        values.put(COLUMN_PFLANZE,      pflanze);
        values.put(COLUMN_AKZESSION,    akzessionId     != -1 ? akzessionId: null);
        values.put(COLUMN_PASSPORT,     passportId      != -1 ? passportId : null);
        if(sorte != null)
            values.put(COLUMN_SORTE,        sorte.isEmpty() ? null: sorte);
        if(zuchtstamm != null)
            values.put(COLUMN_ZUCHTSTAMM,   zuchtstamm.isEmpty() ? null: zuchtstamm);
        if(mutter != null)
            values.put(COLUMN_MUTTER,       mutter.isEmpty() ? null: mutter);
        if(vater != null)
            values.put(COLUMN_VATER,        vater.isEmpty() ? null: vater);
        if(sortimentsnummer != null)
            values.put(COLUMN_SORTIMENTNR,  sortimentsnummer.isEmpty() ? null: sortimentsnummer);

        id = saveRow(id,values);
        return id==-1;
    }

    public static Standort findByPk(int id){
        Standort res = new Standort();

        Cursor c = BoniturSafe.db.rawQuery(
                "SELECT * FROM "+ Standort.TABLE_NAME + " WHERE " + Standort.COLUMN_ID + " = ?",
                new String[] {""+id}
        );

        if(c.getCount() == 1){
            c.moveToFirst();
            res.id =        c.getInt(c.getColumnIndex(Standort.COLUMN_ID));
            res.versuchId = c.getInt(c.getColumnIndex(Standort.COLUMN_VERSUCH));
            res.parzelle = c.getString(c.getColumnIndex(Standort.COLUMN_PARZELLE));
            res.reihe = c.getInt(c.getColumnIndex(Standort.COLUMN_REIHE));
            res.pflanze = c.getInt(c.getColumnIndex(Standort.COLUMN_PFLANZE));
            res.sorte = c.getString(c.getColumnIndex(Standort.COLUMN_SORTE));
            res.zuchtstamm = c.getString(c.getColumnIndex(Standort.COLUMN_ZUCHTSTAMM));
            res.mutter = c.getString(c.getColumnIndex(Standort.COLUMN_MUTTER));
            res.vater = c.getString(c.getColumnIndex(Standort.COLUMN_VATER));
            res.sortimentsnummer = c.getString(c.getColumnIndex(Standort.COLUMN_SORTIMENTNR));
            res.akzessionId = c.getInt(c.getColumnIndex(Standort.COLUMN_AKZESSION));
            res.passportId = c.getInt(c.getColumnIndex(Standort.COLUMN_PASSPORT));
            res.freifeld = c.isNull(c.getColumnIndex(Standort.COLUMN_FREIFELD)) ? "" : c.getString(c.getColumnIndex(Standort.COLUMN_FREIFELD));

            return res;
        }

        return null;
    }

    public String getName(){
        return (this.parzelle + "-" + String.format("%03d-%03d", this.reihe, this.pflanze));
    }


    public String getDate(int markerId){
        Cursor c = BoniturSafe.db.query(
                VersuchWert.TABLE_NAME,
                new String[]{ VersuchWert.COLUMN_WERT_DATUM },
                VersuchWert.COLUMN_STANDORT+"=? AND "+VersuchWert.COLUMN_MARKER+"=?",
                new String[] {""+this.id, ""+markerId},
                null,
                null,
                VersuchWert.COLUMN_ID + " DESC");

        if(c.getCount() == 0)
            return "";

        c.moveToFirst();

        if(c.getCount() >= 1){
            return c.getString(c.getColumnIndex(VersuchWert.COLUMN_WERT_DATUM));
        }

        return "";

    }

    public String getValue(int markerId)
    {
        Cursor c = BoniturSafe.db.query(
                VersuchWert.TABLE_NAME,
                new String[]{ VersuchWert.COLUMN_ID },
                VersuchWert.COLUMN_STANDORT+"=? AND "+VersuchWert.COLUMN_MARKER+"=?",
                new String[] {""+this.id, ""+markerId},
                null,
                null,
                null);

        if(c.getCount() == 0)
            return "";

        c.moveToFirst();

        if(c.getCount() == 1){
            VersuchWert vw = VersuchWert.findByPk(c.getInt(c.getColumnIndex(VersuchWert.COLUMN_ID)));
            if(vw.wert_id > 0)
                return ""+MarkerWert.findByPk(vw.wert_id).value;
            if(vw.wert_text != null)
                return  vw.wert_text;
            return ""+vw.wert_int;
        }

        String res = "";

        do{
            VersuchWert vw = VersuchWert.findByPk(c.getInt(c.getColumnIndex(VersuchWert.COLUMN_ID)));
            res = res + MarkerWert.findByPk(vw.wert_id).value + "|";
        }while (c.moveToNext());

        return res.substring(0,res.length()-1);
    }

}
