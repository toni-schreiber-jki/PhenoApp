package de.bund.jki.jki_bonitur.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Toni on 10.05.2015.
 */
public class Config {

    public static String NAME_ZICK_ZACK_MODUS   = "zickzackmodus";
    public static String NAME_BASE_FOLDER       = "baseFolder";
    public static String NAME_FIRST_EMPTY       = "firstempty";
    public static String NAME_SHOW_ELTERN       = "eltern";
    public static String NAME_SHOW_AKZESSION    = "akzession";
    public static String NAME_SHOW_PASSPORT     = "passport";
    public static String NAME_SHOW_SORTE        = "sorte";
    public static String NAME_SHOW_SORTIMENT    = "sorte";
    public static String NAME_EXCEL_DATUM       = "excelDatum";

    public static String BaseFolder             = "/obstBonitur";
    public static boolean ZICK_ZACK_MODUS       = true;
    public static boolean FIRST_EMPTY           = true;
    public static boolean SHOW_ELTERN           = true;
    public static boolean SHOW_AKZESSION        = true;
    public static boolean SHOW_PASSPORT         = true;
    public static boolean SHOW_SORTE            = true;
    public static boolean SHOW_SORTIMENT        = true;
    public static boolean SHOW_EXCEL_DATUM      = true;

    public static void load(Context c){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        ZICK_ZACK_MODUS = preferences.getBoolean(NAME_ZICK_ZACK_MODUS,ZICK_ZACK_MODUS);
        BaseFolder = preferences.getString(NAME_BASE_FOLDER,BaseFolder);
    }
}
