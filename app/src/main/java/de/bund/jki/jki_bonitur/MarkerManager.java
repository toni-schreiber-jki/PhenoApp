package de.bund.jki.jki_bonitur;

import android.database.Cursor;

import de.bund.jki.jki_bonitur.db.Marker;

/**
 * Created by toni.schreiber on 17.06.2015.
 */
public class MarkerManager {

    public static int MARKER_OK                 = 1;
    public static int MARKER_NEXT_STANDORT      = 2;
    public static int MARKER_NICHT_VORHANDEN    = 3;

    public static Object[] next()
    {
        if(BoniturSafe.CURRENT_MARKER == -1)
            return first();

        Object[] tmpResult = nextMarker();

        if(tmpResult[1]==MARKER_OK)
            return tmpResult;
        if(tmpResult[1]==MARKER_NEXT_STANDORT)
            return first(MARKER_NEXT_STANDORT);

        return null;
    }

    private static Object[] nextMarker(){
        Cursor c = BoniturSafe.db.query(
                Marker.TABLE_NAME,
                new String[]{Marker.COLUMN_ID},
                Marker.COLUMN_VERSUCH+"=? AND " + Marker.COLUMN_ID + " > ?",
                new String[] {""+BoniturSafe.VERSUCH_ID, ""+BoniturSafe.CURRENT_MARKER},
                null,
                null,
                Marker.COLUMN_ID + " ASC");

        if(c.getCount() == 1) {
            c.moveToFirst();
            BoniturSafe.CURRENT_MARKER = c.getInt(c.getColumnIndex(Marker.COLUMN_ID));
            return new Object[] {Marker.findByPk(c.getInt(c.getColumnIndex(Marker.COLUMN_ID))),MARKER_OK};
        }

        return new Object[]{null, MARKER_NEXT_STANDORT};
    }

    private static Object[] first() {
        return first(MARKER_OK);
    }

    private static Object[] first(int flag)
    {
        Cursor c = BoniturSafe.db.query(
                Marker.TABLE_NAME,
                new String[]{Marker.COLUMN_ID},
                Marker.COLUMN_VERSUCH+"=?",
                new String[] {""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                Marker.COLUMN_ID + " ASC");

        if(c.getCount() == 1) {
            c.moveToFirst();
            BoniturSafe.CURRENT_MARKER = c.getInt(c.getColumnIndex(Marker.COLUMN_ID));
            return new Object[] {Marker.findByPk(c.getInt(c.getColumnIndex(Marker.COLUMN_ID))),flag};
        }

        if(flag == MARKER_NEXT_STANDORT)
            return new Object[]{null, MARKER_NICHT_VORHANDEN};
        return new Object[]{null, MARKER_NEXT_STANDORT};
    }
}
