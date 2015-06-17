package de.bund.jki.jki_bonitur;

import android.database.Cursor;
import android.view.animation.BounceInterpolator;

import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.MarkerWert;
import de.bund.jki.jki_bonitur.db.Standort;

/**
 * Created by toni.schreiber on 15.06.2015.
 */
public class StandortManager {

    private static String[] columns =  {Standort.COLUMN_ID, Standort.COLUMN_PARZELLE, Standort.COLUMN_REIHE, Standort.COLUMN_PFLANZE};

    public static Object[] next()
    {
        if(BoniturSafe.CURRENT_PFLANZE == -1)
            return first();

        Object[] marker = MarkerManager.next();
        if(marker[1]==MarkerManager.MARKER_OK)
            return sameStandort(marker[0]);

        if(marker[1]== MarkerManager.MARKER_NEXT_STANDORT)
            return nextStandort();

        return null;
    }

    public static Object[] nextStandort()
    {
        Cursor c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                columns,
                Standort.COLUMN_VERSUCH+"=? AND " + Standort.COLUMN_PARZELLE + "= ?" + Standort.COLUMN_REIHE + " = ? AND " + Standort.COLUMN_REIHE + " > ?" ,
                new String[]{""+BoniturSafe.VERSUCH_ID, ""+BoniturSafe.CURRENT_PARZELLE,  ""+BoniturSafe.CURRENT_REIHE, ""+BoniturSafe.CURRENT_PFLANZE},
                null,
                null,
                Standort.COLUMN_PARZELLE+" ASC, " + Standort.COLUMN_REIHE + " ASC, " + Standort.COLUMN_PFLANZE+ " ASC",
                ""+1);
        if (c.getCount() == 1)
          return getValuesFromStandort(c);

        return nextReihe();
    }

    public static Object[] nextReihe()
    {
        Cursor c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                columns,
                Standort.COLUMN_VERSUCH+"=? AND " + Standort.COLUMN_PARZELLE + "= ?" + Standort.COLUMN_REIHE + " > ? ",
                new String[]{""+BoniturSafe.VERSUCH_ID, ""+BoniturSafe.CURRENT_PARZELLE,  ""+BoniturSafe.CURRENT_REIHE},
                null,
                null,
                Standort.COLUMN_PARZELLE+" ASC, " + Standort.COLUMN_REIHE + " ASC, " + Standort.COLUMN_PFLANZE+ " ASC",
                ""+1);
        return getValuesFromStandort(c);
    }

    private static Object[] sameStandort(Object marker)
    {
        return new Object[]{
                getStandortFromId(BoniturSafe.CURRENT_STANDORT_ID),
                marker
        };
    }

    private static Object[] first()
    {
        Cursor c = BoniturSafe.db.query(
                Standort.TABLE_NAME,
                columns,
                Standort.COLUMN_VERSUCH+"=?",
                new String[]{""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                Standort.COLUMN_PARZELLE+" ASC, " + Standort.COLUMN_REIHE + " ASC, " + Standort.COLUMN_PFLANZE+ " ASC",
                ""+1);

        return getValuesFromStandort(c);
    }

    private static Object[] getValuesFromStandort(Cursor c)
    {
        Object[] res = new Object[2];

        if(c.getCount() == 1) {
            c.moveToFirst();
            res[0] = getStandortFromId(c.getInt(c.getColumnIndex(Standort.COLUMN_ID)));
        }

        res[1] = MarkerManager.next();

        return res;
    }

    private static Object getStandortFromId(int id)
    {
        Standort s = Standort.findByPk(id);

        BoniturSafe.CURRENT_PARZELLE = s.parzelle;
        BoniturSafe.CURRENT_REIHE = s.reihe;
        BoniturSafe.CURRENT_PFLANZE = s.pflanze;

        return s;
    }

}
