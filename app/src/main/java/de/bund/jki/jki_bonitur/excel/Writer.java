package de.bund.jki.jki_bonitur.excel;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import de.bund.jki.jki_bonitur.BoniturSafe;
import de.bund.jki.jki_bonitur.ErrorLog;
import de.bund.jki.jki_bonitur.MarkerManager;
import de.bund.jki.jki_bonitur.StandortManager;
import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.dialoge.WartenDialog;

/**
 * Created by toni.schreiber on 19.06.2015.
 */
public class Writer{

    public Writer(String s)
    {
        writeResult(s);
    }


    public void writeResult(String filePath)
    {
        try {
            //String filePath = mFile.replaceFirst("/in/", "/" + folder + "/");

            AsyncTask execute = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    WartenDialog wd = new WartenDialog(BoniturSafe.BON_ACTIVITY, WartenDialog.MELDUNG_SPEICHERN);

                    HSSFWorkbook workbook = new HSSFWorkbook();
                    workbook = ((Writer) params[0]).addReportFinal(workbook);
                    ExcelLib.writeWorkbook(workbook, ((String) params[1]));


                    wd.close();

                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                }
            };

            execute.execute(this,filePath);

        } catch (Exception e){
            new ErrorLog(e,null);
            e.printStackTrace();
        }
    }

    private HSSFWorkbook addReportFinal(HSSFWorkbook workbook)
    {
        try {
            HSSFSheet sheet = workbook.createSheet("Report");
            sheet.createFreezePane(9, 0);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BoniturSafe.APP_CONTEXT);
            boolean show_datum = preferences.getBoolean(Config.NAME_EXCEL_DATUM, Config.SHOW_EXCEL_DATUM);

            //Überschriften erstellen
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Parzelle");
            row.createCell(1).setCellValue("Reihe");
            row.createCell(2).setCellValue("Pflanze");
            row.createCell(3).setCellValue("Akzessionsnummer");
            row.createCell(4).setCellValue("Akzessionsname");
            row.createCell(5).setCellValue("Kennnr");
            row.createCell(6).setCellValue("Leitname");
            row.createCell(7).setCellValue("StandortInfo");
            row.createCell(8).setCellValue("Charkteristische Merkmale");

            Marker[] marker = MarkerManager.getAllMarker();
            Standort[] standorte = StandortManager.getAllStandorte();

            int p = 0;
            for (Marker m : marker) {
                if (show_datum) {
                    row.createCell(2 * p + 9).setCellValue(m.code);
                } else {
                    row.createCell(p + 9).setCellValue(m.code);
                }
                p++;
            }
            //ENDE: Überschriften erstellen

            //Werte einfügen
            int r = 1;


            for (Standort standort : standorte) {

                Akzession akzession = null;
                Passport passport = null;

                if (standort.akzessionId != -1 && standort.akzessionId != 0)
                    akzession = Akzession.findByPk(standort.akzessionId);
                if (standort.passportId != -1 && standort.passportId != 0)
                    passport = Passport.findByPk(standort.passportId);

                row = sheet.createRow(r);
                row.createCell(0).setCellValue(standort.parzelle);
                row.createCell(1).setCellValue(standort.reihe);
                row.createCell(2).setCellValue(standort.pflanze);
                row.createCell(3).setCellValue(akzession != null ? akzession.nummer : "");
                row.createCell(4).setCellValue(akzession != null ? akzession.name : "");
                row.createCell(5).setCellValue(passport != null ? passport.kennNr : "");
                row.createCell(6).setCellValue(passport != null ? passport.leitname : "");
                row.createCell(7).setCellValue(standort.info != null ? standort.info : "");
                row.createCell(8).setCellValue(akzession != null ? akzession.merkmale : "");


                int mp = 0;
                for (Marker m : marker) {
                    if (show_datum) {
                        row.createCell(2 * mp + 9).setCellValue(standort.getValue(m.id));
                        row.createCell(2 * mp + 1 + 9).setCellValue(standort.getDate(m.id));
                    } else {
                        row.createCell(mp + 9).setCellValue(standort.getValue(m.id));
                    }
                    mp++;
                }
                r++;
                if(r%25 == 0) {
                    try {
                        System.gc();
                    } catch (Exception e) {
                        new ErrorLog(e, BoniturSafe.APP_CONTEXT);
                    }
                }
            }
        }catch (Exception e){
            new ErrorLog(e, BoniturSafe.APP_CONTEXT);
        }

        return workbook;
    }
}
