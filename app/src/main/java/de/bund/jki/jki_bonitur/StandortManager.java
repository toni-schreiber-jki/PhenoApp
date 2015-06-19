package de.bund.jki.jki_bonitur;

import android.database.Cursor;
import android.view.animation.BounceInterpolator;

import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.MarkerWert;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;

/**
 * Created by toni.schreiber on 15.06.2015.
 */
public class StandortManager {

    private static int NEXT = 1;
    private static int PREV = 2;

    private static String[] columns =  {Standort.COLUMN_ID, Standort.COLUMN_PARZELLE, Standort.COLUMN_REIHE, Standort.COLUMN_PFLANZE};

    public static Object[] next(){
        return prevNext(NEXT);
    }

    public static Object[] prev(){
        return prevNext(PREV);
    }

    private static Object[] prevNext(int direction){
        if(BoniturSafe.CURRENT_PFLANZE == -1)
            return first();

        Object[] marker = direction == NEXT ? MarkerManager.next() : MarkerManager.prev();
        if(((int)marker[1])==MarkerManager.MARKER_OK)
            return sameStandort(marker[0]);

        if(((int)marker[1])== MarkerManager.MARKER_NEXT_STANDORT)
            return direction == NEXT ? nextStandort(NEXT, marker[0]) : nextStandort(PREV, marker[0]);

        return null;
    }

    public static Object[] nextStandort(Object marker){
        return nextStandort(NEXT, marker);
    }

    public static Object[] nextStandort(int direction, Object marker) {
        try {
            Cursor c = BoniturSafe.db.query(
                    Standort.TABLE_NAME,
                    columns,
                    Standort.COLUMN_VERSUCH + "=? AND " + Standort.COLUMN_PARZELLE + "= ? AND " + Standort.COLUMN_REIHE + " = ? AND " + Standort.COLUMN_PFLANZE + " " + (direction == NEXT ? ">" : "<") + "  ?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.CURRENT_PARZELLE, "" + BoniturSafe.CURRENT_REIHE, ""+BoniturSafe.CURRENT_PFLANZE},
                    null,
                    null,
                    Standort.COLUMN_PARZELLE + " ASC, CAST(" + Standort.COLUMN_REIHE + " AS INTEGER) ASC, CAST(" + Standort.COLUMN_PFLANZE + " AS INTEGER) " + (direction == NEXT ? "ASC" : "DESC"),
                    "" + 1);
            if (c.getCount() == 1)
                return new Object[]{getValuesFromStandort(c), marker};

            return nextReihe(direction, marker);
        }catch (Exception e)
        {
            int a = 1;
            e.printStackTrace();
            return null;
        }
    }

    public static Object[] nextReihe(Object marker){
        return nextReihe(NEXT,marker);
    }

    public static Object[] nextReihe(int direction, Object marker) {
        Cursor c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                columns,
                Standort.COLUMN_VERSUCH+"=? AND " + Standort.COLUMN_PARZELLE + "= ? AND " + Standort.COLUMN_REIHE + " "+(direction == NEXT ? ">" : "<")+" ? ",
                new String[]{""+BoniturSafe.VERSUCH_ID, ""+BoniturSafe.CURRENT_PARZELLE,  ""+BoniturSafe.CURRENT_REIHE},
                null,
                null,
                "CAST(" + Standort.COLUMN_REIHE + " AS INTEGER) "+(direction == NEXT ? "ASC" : "DESC")+", CAST(" + Standort.COLUMN_PFLANZE+ " AS INTEGER) "+(direction == NEXT ? "ASC" : "DESC"),
                ""+1);
        return new Object[] {getValuesFromStandort(c), marker};
    }

    public static Object[] sameStandort(Object marker){
        return new Object[]{
                getStandortFromId(BoniturSafe.CURRENT_STANDORT_ID),
                marker
        };
    }

    private static Object[] first(){
        Cursor c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                columns,
                Standort.COLUMN_VERSUCH+"=?",
                new String[]{""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                Standort.COLUMN_PARZELLE+" ASC, " + Standort.COLUMN_REIHE + " ASC, " + Standort.COLUMN_PFLANZE+ " ASC",
                ""+1);

        return new Object[] {getValuesFromStandort(c), MarkerManager.next()[0]};
    }

    private static Object getValuesFromStandort(Cursor c){
        if(c.getCount() == 1) {
            c.moveToFirst();
            return getStandortFromId(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
        }

        return null;
    }

    private static Object getStandortFromId(int id){
        Standort s = Standort.findByPk(id);

        BoniturSafe.CURRENT_PARZELLE    = s.parzelle;
        BoniturSafe.CURRENT_REIHE       = s.reihe;
        BoniturSafe.CURRENT_PFLANZE     = s.pflanze;
        BoniturSafe.CURRENT_STANDORT_ID = id;

        return s;
    }

    public static Akzession[] getAllAkzessionen(){
        Akzession[] res = new Akzession[0];

        Cursor c = BoniturSafe.db.query(
                Akzession.TABLE_NAME,
                new String[] {Akzession.COLUMN_ID},
                Akzession.COLUMN_VERSUCH+"=?",
                new String[] {""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                Akzession.COLUMN_NAME + " ASC"
                );

        if(c.getCount() > 0) {
            res = new Akzession[c.getCount()];
            int p = 0;
            c.moveToFirst();
            do{
                res[p] = Akzession.findByPk(c.getInt(c.getColumnIndex(Akzession.COLUMN_ID)));
                p++;
            }while (c.moveToNext());
        }

        return res;
    }

    public static Passport[] getAllPassport(){
        Passport[] res = new Passport[0];

        Cursor c = BoniturSafe.db.query(
                Passport.TABLE_NAME,
                new String[] {Passport.COLUMN_ID},
                Passport.COLUMN_VERSUCH+"=?",
                new String[] {""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                Passport.COLUMN_LEITNAME + " ASC"
        );

        if(c.getCount() > 0) {
            res = new Passport[c.getCount()];
            int p = 0;
            c.moveToFirst();
            do{
                res[p] = Passport.findByPk(c.getInt(c.getColumnIndex(Passport.COLUMN_ID)));
                p++;
            }while (c.moveToNext());
        }

        return res;
    }

    public static Standort[] getAllStandorte(){
        Standort[] res = new Standort[0];

        Cursor c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                new String[] {Standort.COLUMN_ID},
                Standort.COLUMN_VERSUCH+"=?",
                new String[] {""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                Standort.COLUMN_PARZELLE + " ASC, CAST(" + Standort.COLUMN_REIHE + " AS INTEGER) ASC, CAST(" + Standort.COLUMN_PFLANZE + " AS INTEGER) ASC"
        );

        if(c.getCount() > 0) {
            res = new Standort[c.getCount()];
            int p = 0;
            c.moveToFirst();
            do{
                res[p] = Standort.findByPk(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
                p++;
            }while (c.moveToNext());
        }

        return res;
    }

}
