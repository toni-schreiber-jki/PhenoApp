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

    private static int NEXT = 1;
    private static int PREV = 2;

    public static Object[] next()
    {
        return prevNext(NEXT);
    }

    public static Object[] prev()
    {
        return prevNext(PREV);
    }

    public static Object[] prevNext(int direction){
        if(BoniturSafe.CURRENT_MARKER == -1)
            return first();

        Object[] tmpResult;
        if(direction == NEXT)
            tmpResult =nextMarker(direction);
        else
            tmpResult =nextMarker(direction);

        if(((int)tmpResult[1])==MARKER_OK)
            return tmpResult;
        if(((int)tmpResult[1])==MARKER_NEXT_STANDORT)
            if(direction == NEXT)
                return first(MARKER_NEXT_STANDORT);
            else
                return last(MARKER_NEXT_STANDORT);

        return null;
    }

    private static Object[] nextMarker(int direction){
        try {
            Cursor c = BoniturSafe.db.query(
                    Marker.TABLE_NAME,
                    new String[]{Marker.COLUMN_ID},
                    Marker.COLUMN_VERSUCH + "=? AND " + Marker.COLUMN_ID + " "+ (direction == NEXT ? ">" : "<") +" ?",
                    new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.CURRENT_MARKER},
                    null,
                    null,
                    "CAST (" + Marker.COLUMN_ID + " AS INTEGER) " + ((direction == NEXT ? "ASC" : "DESC")),
                    "" + 1);

            if (c.getCount() == 1) {
                c.moveToFirst();
                BoniturSafe.CURRENT_MARKER = c.getInt(c.getColumnIndex(Marker.COLUMN_ID));
                return new Object[]{Marker.findByPk(c.getInt(c.getColumnIndex(Marker.COLUMN_ID))), MARKER_OK};
            }

            return new Object[]{null, MARKER_NEXT_STANDORT};
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static Object[] first() {
        return first(MARKER_OK);
    }

    private static Object[] first(int flag)
    {
        return getFirstLast(flag , "ASC");
    }

    private static Object[] last(int flag)
    {
        return getFirstLast(flag , "DESC");
    }

    private static Object[] getFirstLast(int flag , String direction ){
        Cursor c = BoniturSafe.db.query(
                Marker.TABLE_NAME,
                new String[]{Marker.COLUMN_ID},
                Marker.COLUMN_VERSUCH+"=?",
                new String[] {""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                "CAST ("+Marker.COLUMN_ID + " AS INTEGER) " +direction,
                ""+1);

        if(c.getCount() == 1) {
            c.moveToFirst();
            BoniturSafe.CURRENT_MARKER = c.getInt(c.getColumnIndex(Marker.COLUMN_ID));
            return new Object[] {Marker.findByPk(c.getInt(c.getColumnIndex(Marker.COLUMN_ID))),flag};
        }

        if(flag == MARKER_NEXT_STANDORT)
            return new Object[]{null, MARKER_NICHT_VORHANDEN};
        return new Object[]{null, MARKER_NEXT_STANDORT};
    }

    public static Marker[] getAllMarker(){
        Cursor c = BoniturSafe.db.query(
                Marker.TABLE_NAME,
                new String[]{Marker.COLUMN_ID},
                Marker.COLUMN_VERSUCH+"=?",
                new String[] {""+BoniturSafe.VERSUCH_ID},
                null,
                null,
                "CAST (" + Marker.COLUMN_ID + " AS INTEGER) ASC");

        Marker[] result = new Marker[c.getCount()];

        if(c.getCount() >= 1) {
            c.moveToFirst();
            int p = 0;
            do {
                result[p] = Marker.findByPk(c.getInt(c.getColumnIndex(Marker.COLUMN_ID)));
                p++;
            }while (c.moveToNext());
        }

        return result;
    }
}
