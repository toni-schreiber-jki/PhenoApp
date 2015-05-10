package de.bund.jki.jki_bonitur.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Toni on 10.05.2015.
 */
public class BoniturDatenbank extends SQLiteOpenHelper {

    public static final String DATENBANK_NAME = "bonitur.sqlite";
    public static final int DATENBANK_VERSION = 7;

    public BoniturDatenbank(Context context) {
         super(
                 context,
                 DATENBANK_NAME,
                 null,
                 DATENBANK_VERSION
         );
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Versuch.CREATE_TABLE);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try
        {
            switch (newVersion)
            {
                case 6: db.execSQL(Marker.CREATE_TABLE); break;
                case 7: db.execSQL(Standort.CREATE_TABLE); break;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
