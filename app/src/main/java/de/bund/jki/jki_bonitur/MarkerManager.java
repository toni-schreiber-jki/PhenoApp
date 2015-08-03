package de.bund.jki.jki_bonitur;

import android.database.Cursor;

import java.util.ArrayList;

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

    public static Object[] next(){
        return prevNext(NEXT);
    }

    public static Object[] prev(){
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
                    Marker.COLUMN_VERSUCH + "=? AND " + Marker.COLUMN_ID + " "+ (direction == NEXT ? ">" : "<") +" ?" + (isMarkerFilterActive() ? " AND "+Marker.COLUMN_ID+" IN ("+getMarkerFilter()+")" :""),
                    getMarkerFilerValues(new String[]{"" + BoniturSafe.VERSUCH_ID, "" + BoniturSafe.CURRENT_MARKER}),
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
        catch (Exception e){
            new ErrorLog(e,null);
            e.printStackTrace();
            return null;
        }
    }

    private static Object[] first() {
        return first(MARKER_OK);
    }

    private static Object[] first(int flag){
        return getFirstLast(flag , "ASC");
    }

    private static Object[] last(int flag){
        return getFirstLast(flag , "DESC");
    }

    private static Object[] getFirstLast(int flag , String direction ){

        Cursor c = BoniturSafe.db.query(
                Marker.TABLE_NAME,
                new String[]{Marker.COLUMN_ID},
                Marker.COLUMN_VERSUCH+"=?" + (isMarkerFilterActive() ? " AND "+Marker.COLUMN_ID+" IN ("+getMarkerFilter()+")" :""),
                getMarkerFilerValues(new String[]{"" + BoniturSafe.VERSUCH_ID}),
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

    private static boolean isMarkerFilterActive(){
        if(!BoniturSafe.MARKER_FILTER_ACTIVE) return false;
        return !BoniturSafe.MARKER_FILTER.isEmpty();
    }

    private static String[] getMarkerFilerValues(String[] values){
        ArrayList<Integer> tmpMarkerFilter = (isMarkerFilterActive()? BoniturSafe.MARKER_FILTER : new ArrayList<Integer>());

        String[] res = new String[tmpMarkerFilter.size() + values.length];
        int p = 0;
        for(String s : values)
        {
            res[p] = s;
            p++;
        }
        for (Integer i : tmpMarkerFilter.toArray(new Integer[] {}))
        {
            res[p] = i.toString();
            p++;
        }
        return res;
    }

    private static String getMarkerFilter(){
        String res = "";
        for(int i =0; i< BoniturSafe.MARKER_FILTER.size(); i++){
            if(i>0)
                res += ",";
            res += "?";
        }
        return res;
    }

    public static Marker getFirstUnusedMarker(int standortId){
        Cursor c = BoniturSafe.db.rawQuery(
                "SELECT _id FROM marker " +
                "WHERE versuchId = ? AND _id not in (" +
                        "SELECT markerId FROM versuchWert WHERE standortId = ?" +
                ") ORDER BY _id ASC LIMIT 1",
                new String[]{""+BoniturSafe.VERSUCH_ID, ""+standortId});

        if(c.getCount()==1){
            c.moveToFirst();
            return Marker.findByPk(c.getInt(c.getColumnIndex(Marker.COLUMN_ID)));
        }

        return null;
    }
}
