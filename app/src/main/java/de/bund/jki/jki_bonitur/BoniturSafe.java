package de.bund.jki.jki_bonitur;

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

    public static SQLiteDatabase db;
}
