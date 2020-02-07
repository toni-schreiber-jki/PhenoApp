package de.bund.jki.jki_bonitur.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;

import de.bund.jki.jki_bonitur.BoniturSafe;

/**
 * Created by Toni on 10.05.2015.
 */
public class Standort extends DbModelInterface {

    public static String COLUMN_ID          = "_id";
    public static String COLUMN_VERSUCH     = "versuchId";
    public static String COLUMN_PARZELLE    = "parzelle";
    public static String COLUMN_REIHE       = "reihe";
    public static String COLUMN_PFLANZE     = "pflanze";
    public static String COLUMN_AKZESSION   = "akzessionId";
    public static String COLUMN_PASSPORT    = "passportId";
    public static String COLUMN_SORTE       = "sorte";
    public static String COLUMN_ZUCHTSTAMM  = "zuchtstamm";
    public static String COLUMN_MUTTER      = "Mutter";
    public static String COLUMN_VATER       = "Vater";
    public static String COLUMN_SORTIMENTNR = "Sortimentsnummer";
    public static String COLUMN_FREIFELD    = "freifeld";
    public static String COLUMN_INFO        = "information";
    public static String COLUMN_DB_KEY      = "db_key";
    public static String TABLE_NAME         = "standort";
    public static String CREATE_TABLE       = "CREATE TABLE " + TABLE_NAME + "(" + '\n' +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + '\n' +
            COLUMN_VERSUCH + " INTEGER NOT NULL," + '\n' +
            COLUMN_PARZELLE + " TEXT NOT NULL DEFAULT '00'," + '\n' +
            COLUMN_REIHE + " INTEGER NOT NULL," + '\n' +
            COLUMN_PFLANZE + " INTEGER NOT NULL," + '\n' +
            COLUMN_SORTE + " TEXT," + '\n' +
            COLUMN_ZUCHTSTAMM + " TEXT," + '\n' +
            COLUMN_MUTTER + " TEXT," + '\n' +
            COLUMN_VATER + " TEXT," + '\n' +
            COLUMN_SORTIMENTNR + " TEXT," + '\n' +
            //COLUMN_AKZESSION+ " INTEGER," + '\n' +
            //COLUMN_PASSPORT + " INTEGER," + '\n' +

            "CONSTRAINT standort_versuch FOREIGN KEY(" + COLUMN_VERSUCH + ") REFERENCES " + Versuch.TABLE_NAME + "(" + Versuch.COLUMN_ID + ")" + '\n' +
            "ON UPDATE CASCADE " + '\n' +
            "ON DELETE CASCADE " + '\n' +
            ")";
    public static String ALTER_TABLE_1      = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_AKZESSION + " INTEGER REFERENCES " + Akzession.TABLE_NAME + "(" + Versuch.COLUMN_ID + ")" + '\n';
    public static String ALTER_TABLE_2      = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PASSPORT + " INTEGER REFERENCES " + Passport.TABLE_NAME + "(" + Versuch.COLUMN_ID + ")" + '\n';
    public static String ALTER_TABLE_3      = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_FREIFELD + " TEXT" + '\n';
    public static String ALTER_TABLE_4      = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_INFO + " TEXT" + '\n';
    public static String ALTER_TABLE_5      = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_DB_KEY + " TEXT" + '\n';

    public int       id          = - 1;
    public int       versuchId   = - 1;
    public String    parzelle    = null;
    public int       reihe       = - 1;
    public int       pflanze     = - 1;
    public int       akzessionId = - 1;
    public int       passportId  = - 1;
    public String    sorte;
    public String    zuchtstamm;
    public String    mutter;
    public String    vater;
    public String    sortimentsnummer;
    public String    freifeld;
    public String    info;
    public String    dbKey;
    public Akzession akzession;
    public Passport  passport;


    public Standort() {
        super.TABLE_NAME = TABLE_NAME;
        super.COLUMN_ID = COLUMN_ID;
    }

    public static Standort findByPk(int id) {
        Standort res = new Standort();
        Cursor   c   = null;
        try {
            c = BoniturSafe.db.rawQuery(
                    "SELECT * FROM " + Standort.TABLE_NAME + " WHERE " + Standort.COLUMN_ID + " = ?",
                    new String[]{"" + id}
            );

            if (c.getCount() == 1) {
                c.moveToFirst();

                res.fillElementWithCurcor(c);
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
        values.put(COLUMN_PARZELLE, parzelle);
        values.put(COLUMN_REIHE, reihe);
        values.put(COLUMN_PFLANZE, pflanze);
        values.put(COLUMN_AKZESSION, akzessionId != - 1 ? akzessionId : null);
        values.put(COLUMN_PASSPORT, passportId != - 1 ? passportId : null);
        if (sorte != null)
            values.put(COLUMN_SORTE, sorte.isEmpty() ? null : sorte);
        if (zuchtstamm != null)
            values.put(COLUMN_ZUCHTSTAMM, zuchtstamm.isEmpty() ? null : zuchtstamm);
        if (mutter != null)
            values.put(COLUMN_MUTTER, mutter.isEmpty() ? null : mutter);
        if (vater != null)
            values.put(COLUMN_VATER, vater.isEmpty() ? null : vater);
        if (sortimentsnummer != null)
            values.put(COLUMN_SORTIMENTNR, sortimentsnummer.isEmpty() ? null : sortimentsnummer);
        if (freifeld != null)
            values.put(COLUMN_FREIFELD, freifeld.isEmpty() ? null : freifeld);

        if (info != null)
            values.put(COLUMN_INFO, info.isEmpty() ? null : info);

        id = saveRow(id, values);
        return id == - 1;
    }

    public String getName() {
        return (this.parzelle + "-" + String.format("%03d-%03d", this.reihe, this.pflanze));
    }


    public String getDate(int markerId) {
        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                    VersuchWert.TABLE_NAME,
                    new String[]{VersuchWert.COLUMN_WERT_DATUM},
                    VersuchWert.COLUMN_STANDORT + "=? AND " + VersuchWert.COLUMN_MARKER + "=?",
                    new String[]{"" + this.id, "" + markerId},
                    null,
                    null,
                    VersuchWert.COLUMN_ID + " DESC"
            );

            if (c.getCount() == 0) {
                //c.close();

                return "";
            }

            c.moveToFirst();

            if (c.getCount() >= 1) {
                String result = c.getString(c.getColumnIndex(VersuchWert.COLUMN_WERT_DATUM));
                //c.close();
                return result;
            }

            //c.close();

            return "";
        } finally {
            if (c != null)
                c.close();
        }

    }

    public String getValue(int markerId) {
        Cursor c = null;
        try {
            c = BoniturSafe.db.query(
                    VersuchWert.TABLE_NAME,
                    null,//new String[]{ VersuchWert.COLUMN_ID },
                    VersuchWert.COLUMN_STANDORT + "=? AND " + VersuchWert.COLUMN_MARKER + "=?",
                    new String[]{"" + this.id, "" + markerId},
                    null,
                    null,
                    null
            );

            if (c.getCount() == 0) {
                //c.close();
                return "";
            }

            c.moveToFirst();

            if (c.getCount() == 1) {
                VersuchWert vw = new VersuchWert(); //.findByPk(c.getInt(c.getColumnIndex(VersuchWert.COLUMN_ID)));
                vw.fillWithCursor(c);
                if (vw.wert_id > 0) {
                    //c.close();
                    return "" + this.getMarkerWert(vw.wert_id).value;
                }
                if (vw.wert_text != null) {
                    //c.close();
                    return vw.wert_text;
                }
                //c.close();
                return "" + vw.wert_int;
            }

            String res = "";

            do {
                VersuchWert vw = new VersuchWert();//.findByPk(c.getInt(c.getColumnIndex(VersuchWert.COLUMN_ID)));
                vw.fillWithCursor(c);
                res = res + this.getMarkerWert(vw.wert_id).value + "|";
            } while (c.moveToNext());

            //c.close();

            return res.substring(0, res.length() - 1);
        } finally {
            if (c != null)
                c.close();
        }
    }

    public void fillElementWithCurcor(Cursor c) {
        this.id = c.getInt(c.getColumnIndex(Standort.COLUMN_ID));
        this.versuchId = c.getInt(c.getColumnIndex(Standort.COLUMN_VERSUCH));
        this.parzelle = c.getString(c.getColumnIndex(Standort.COLUMN_PARZELLE));
        this.reihe = c.getInt(c.getColumnIndex(Standort.COLUMN_REIHE));
        this.pflanze = c.getInt(c.getColumnIndex(Standort.COLUMN_PFLANZE));
        this.sorte = c.getString(c.getColumnIndex(Standort.COLUMN_SORTE));
        this.zuchtstamm = c.getString(c.getColumnIndex(Standort.COLUMN_ZUCHTSTAMM));
        this.mutter = c.getString(c.getColumnIndex(Standort.COLUMN_MUTTER));
        this.vater = c.getString(c.getColumnIndex(Standort.COLUMN_VATER));
        this.sortimentsnummer = c.getString(c.getColumnIndex(Standort.COLUMN_SORTIMENTNR));
        this.akzessionId = c.getInt(c.getColumnIndex(Standort.COLUMN_AKZESSION));
        this.passportId = c.getInt(c.getColumnIndex(Standort.COLUMN_PASSPORT));
        this.freifeld = c.isNull(c.getColumnIndex(Standort.COLUMN_FREIFELD)) ? "" : c.getString(c.getColumnIndex(Standort.COLUMN_FREIFELD));
        this.info = c.isNull(c.getColumnIndex(Standort.COLUMN_INFO)) ? "" : c.getString(c.getColumnIndex(Standort.COLUMN_INFO));
        this.dbKey = c.isNull(c.getColumnIndex(Standort.COLUMN_DB_KEY)) ? "" : c.getString(c.getColumnIndex(Standort.COLUMN_DB_KEY));
    }

    public void fillAkzession(Cursor c) {
        if (this.akzessionId >= 1) {
            this.akzession = new Akzession();
            this.akzession.id = this.akzessionId;
            this.akzession.nummer = c.getString(c.getColumnIndex(Akzession.COLUMN_NUMMER));
            this.akzession.name = c.getString(c.getColumnIndex(Akzession.COLUMN_NAME));
            this.akzession.merkmale = c.getString(c.getColumnIndex("aMerkmale"));
        }
    }

    public void fillPassport(Cursor c) {
        if (this.passportId >= 1) {
            this.passport = new Passport();
            this.passport.kennNr = c.getString(c.getColumnIndex(Passport.COLUMN_KENN_NR));
            this.passport.leitname = c.getString(c.getColumnIndex(Passport.COLUMN_LEITNAME));
            this.passport.merkmale = c.getString(c.getColumnIndex("pMerkmale"));
        }
    }

    private MarkerWert getMarkerWert(int id) {
        MarkerWert res = null;
        if (BoniturSafe.markerWertHashMap == null) {
            BoniturSafe.markerWertHashMap = new HashMap<Integer, MarkerWert>();
        }
        if (BoniturSafe.markerWertHashMap.containsKey(id)) {
            res = BoniturSafe.markerWertHashMap.get(id);
        } else {
            res = MarkerWert.findByPk(id);
            BoniturSafe.markerWertHashMap.put(id, res);
        }
        return res;
    }
}
