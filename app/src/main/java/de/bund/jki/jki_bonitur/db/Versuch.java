package de.bund.jki.jki_bonitur.db;

/**
 * Created by Toni on 10.05.2015.
 */
public class Versuch extends DbModelInterface {

    public static String COLUMN_ID = "_id";
    public static String COLUMN_NAME = "name";

    public static String TABLE_NAME = "versuch";

    public static String CREATE_TABLE = "CREATE TABLE "+ Versuch.TABLE_NAME + "(" + '\n'+
            Versuch.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +'\n'+
            Versuch.COLUMN_NAME + " VARCHAR(100) NOT NULL" + '\n' +
    ")";

}
