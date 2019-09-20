package de.bund.jki.jki_bonitur.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

/**
 * Created by Toni on 10.05.2015.
 */
public class Config {

    public static String NAME_ZICK_ZACK_MODUS = "zickzackmodus";
    public static String NAME_BASE_FOLDER    = "baseFolder";
    public static String NAME_FIRST_EMPTY     = "firstempty";
    public static String NAME_SHOW_ELTERN     = "eltern";
    public static String NAME_SHOW_AKZESSION  = "akzession";
    public static String NAME_SHOW_PASSPORT   = "passport";
    public static String NAME_SHOW_SORTE      = "sorte";
    public static String NAME_SHOW_SORTIMENT  = "sorte";
    public static String NAME_EXCEL_DATUM     = "excelDatum";
    public static String NAME_BBCH_FRAGE      = "bbchFrage";
    public static String NAME_LEFT_HAND_MODE  = "linkshaender";
    public static String NAME_MULTIPLE_VALUES = "multipleValues";

    public static String  BaseFolder         = File.separator + "obstBonitur";
    public static boolean ZICK_ZACK_MODUS    = true;
    public static boolean FIRST_EMPTY        = true;
    public static boolean SHOW_ELTERN        = true;
    public static boolean SHOW_AKZESSION     = true;
    public static boolean SHOW_PASSPORT      = true;
    public static boolean SHOW_SORTE         = true;
    public static boolean SHOW_SORTIMENT     = true;
    public static boolean SHOW_EXCEL_DATUM   = true;
    public static boolean SHOW_BBCH_FRAGE    = true;
    public static boolean IS_LEFT_HAND_MODE  = false;
    public static boolean IS_MULTIPLE_VALUES = true;

    public static void load(Context c) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        ZICK_ZACK_MODUS = preferences.getBoolean(NAME_ZICK_ZACK_MODUS, ZICK_ZACK_MODUS);
        BaseFolder = preferences.getString(NAME_BASE_FOLDER, BaseFolder);
        FIRST_EMPTY = preferences.getBoolean(NAME_FIRST_EMPTY, FIRST_EMPTY);
        SHOW_ELTERN = preferences.getBoolean(NAME_SHOW_ELTERN, SHOW_ELTERN);
        SHOW_AKZESSION = preferences.getBoolean(NAME_SHOW_AKZESSION, SHOW_AKZESSION);
        SHOW_PASSPORT = preferences.getBoolean(NAME_SHOW_PASSPORT, SHOW_PASSPORT);
        SHOW_SORTE = preferences.getBoolean(NAME_SHOW_SORTE, SHOW_SORTE);
        SHOW_SORTIMENT = preferences.getBoolean(NAME_SHOW_SORTIMENT, SHOW_SORTIMENT);
        SHOW_EXCEL_DATUM = preferences.getBoolean(NAME_EXCEL_DATUM, SHOW_EXCEL_DATUM);
        SHOW_BBCH_FRAGE = preferences.getBoolean(NAME_BBCH_FRAGE, SHOW_BBCH_FRAGE);
        IS_LEFT_HAND_MODE = preferences.getBoolean(NAME_LEFT_HAND_MODE, IS_LEFT_HAND_MODE);
        IS_MULTIPLE_VALUES = preferences.getBoolean(NAME_MULTIPLE_VALUES, IS_MULTIPLE_VALUES);
    }
}
