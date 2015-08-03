package de.bund.jki.jki_bonitur.tools;

import android.text.format.DateFormat;

/**
 * Created by toni.schreiber on 03.08.2015.
 */
public class DateTool {
    public static String currentDate(){
        return ""+DateFormat.format("dd.MM.yyyy", new java.util.Date());
    }
}
