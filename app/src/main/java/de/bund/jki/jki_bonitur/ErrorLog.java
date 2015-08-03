package de.bund.jki.jki_bonitur;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.bund.jki.jki_bonitur.config.Config;

/**
 * Created by toni.schreiber on 26.06.2015.
 */
public class ErrorLog {
    public ErrorLog(Exception e, Context c)
    {
        try {
            String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            File storageDir = new File(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/error/");
            File errorFile = new File(Environment.getExternalStorageDirectory().toString() + Config.BaseFolder + "/error/" + timeStamp + ".txt");
            if(!storageDir.exists()) storageDir.mkdir();
            errorFile.createNewFile();
            PrintStream ps = new PrintStream(errorFile);
            e.printStackTrace(ps);
            ps.close();
            if(c==null) {
                c = BoniturSafe.APP_CONTEXT;
            }
            if(c != null){
                Toast.makeText(c,"Es ist ein interner Fehler aufgetretten, ggf toni.schreiber@jki.bund.de kontaktieren. Ein Fehlerbericht wurde aufgezeichnet.",Toast.LENGTH_LONG).show();
            }

        }catch (Exception ee){

        }
    }
}
