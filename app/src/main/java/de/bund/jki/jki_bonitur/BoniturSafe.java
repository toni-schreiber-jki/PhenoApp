package de.bund.jki.jki_bonitur;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Toni on 11.05.2015.
 */
public class BoniturSafe {

    public static String    VERSUCH_NAME;
    public static int       VERSUCH_ID;

    public static String    CURRENT_PARZELLE;
    public static int       CURRENT_REIHE;
    public static int       CURRENT_PFLANZE;
    public static int       CURRENT_STANDORT_ID;

    public static int       CURRENT_MARKER;

    public static ArrayList<Integer> MARKER_FILTER;
    public static boolean   MARKER_FILTER_ACTIVE = true;

    public static int       PFLANZEN_RICHTUNG   = StandortManager.NEXT;
    public static int       REIHEN_RICHTUNG     = StandortManager.NEXT;

    public static Context   APP_CONTEXT = null;
    public static BoniturActivity BON_ACTIVITY = null;

    public static int       FIRST_EMPTY_COUNT = 1;

    public static SQLiteDatabase db;
}
