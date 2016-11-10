package de.bund.jki.jki_bonitur.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import de.bund.jki.jki_bonitur.ErrorLog;
import de.bund.jki.jki_bonitur.config.Config;


/**
 * Created by Toni on 10.05.2015.
 */
public class BoniturDatenbank extends SQLiteOpenHelper {

    public static final String DATENBANK_NAME = "bonitur.sqlite";
    public static final int DATENBANK_VERSION = 3;
    public Context c;

    public BoniturDatenbank(Context context) {
        super(
                 context,
                 DATENBANK_NAME,
                 null,
                 DATENBANK_VERSION
         );
        c=context;
    }

    public void copy_to_sd() throws IOException {
        File inputFile = new File("/data/data/de.bund.jki.jki_bonitur/databases/"+DATENBANK_NAME);
        File outputFile = new File(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder +"/"+DATENBANK_NAME);
        File importDB = new File(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder +"/import/"+DATENBANK_NAME);


        FileReader in = null;
        FileWriter out = null;
        try {
            in = new FileReader(inputFile);
        } catch (FileNotFoundException e) {
            // Es gibt keine gespeicherte DB
        }
        if(in != null)
        {
            try {
                out = new FileWriter(outputFile);
            } catch (IOException e) {
                // Problem beim speichern der DB auf der Karte
            }

        }

        if(in != null && out != null)
        {
            try {
                copyFile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

            } catch (IOException e) {
                // Problem beim kopieren der DB
                Toast.makeText(c, "Beim kopieren der DB ist ein Fehler aufgetreten..!", Toast.LENGTH_LONG).show();
            }

            //Toast.makeText(c, "Die DB wurde erfolgreich kopiert..!", Toast.LENGTH_LONG).show();
        }

        try {
            if (importDB.exists()) {
                copyFile(importDB.getAbsolutePath(), inputFile.getAbsolutePath());
                importDB.delete();
            }
        } catch (IOException e) {
            Toast.makeText(c, "Beim import der DB ist ein Fehler aufgetreten..!", Toast.LENGTH_LONG).show();
        }
    }

    void copyFile(String from, String to)throws IOException{
        FileChannel in = new FileInputStream(from).getChannel();
        FileChannel out = new FileOutputStream(to).getChannel();
        in.transferTo(0, in.size(), out);
        in.close();
        out.close();
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
            for(int v=0; v <= this.DATENBANK_VERSION; v++){
                this.onUpgrade(db,v,v+1);
            }
        } catch (Exception e)
        {
            new ErrorLog(e,null);
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try
        {
            switch (newVersion)
            {
                case 2: db.execSQL(Standort.ALTER_TABLE_3); break;
                case 3: db.execSQL(Standort.ALTER_TABLE_4); break;
            }
        }catch (Exception e)
        {
            new ErrorLog(e,null);
            e.printStackTrace();
        }
    }
}
