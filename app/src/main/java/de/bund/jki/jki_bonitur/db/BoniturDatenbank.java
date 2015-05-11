package de.bund.jki.jki_bonitur.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Toni on 10.05.2015.
 */
public class BoniturDatenbank extends SQLiteOpenHelper {

    public static final String DATENBANK_NAME = "bonitur.sqlite";
    public static final int DATENBANK_VERSION = 1;

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
            db.execSQL(Marker.CREATE_TABLE);
            db.execSQL(Standort.CREATE_TABLE);
            db.execSQL(MarkerWert.CREATE_TABLE);
            db.execSQL(VersuchWert.CREATE_TABLE);
            db.execSQL(Passport.CREATE_TABLE);
            db.execSQL(Akzession.CREATE_TABLE);
            db.execSQL(Standort.ALTER_TABLE_1);
            db.execSQL(Standort.ALTER_TABLE_2);
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


            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
