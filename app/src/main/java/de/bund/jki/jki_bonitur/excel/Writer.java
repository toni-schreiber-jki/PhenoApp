package de.bund.jki.jki_bonitur.excel;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;

import de.bund.jki.jki_bonitur.BoniturSafe;
import de.bund.jki.jki_bonitur.ErrorLog;
import de.bund.jki.jki_bonitur.MarkerManager;
import de.bund.jki.jki_bonitur.R;
import de.bund.jki.jki_bonitur.StandortManager;
import de.bund.jki.jki_bonitur.config.Config;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;
import de.bund.jki.jki_bonitur.db.VersuchWert;
import de.bund.jki.jki_bonitur.dialoge.WartenDialog;

/**
 * Created by toni.schreiber on 19.06.2015.
 */
public class Writer {

    public static final String WORKSHEET_DATA_NAME = "data";
    public static final int FORMAT_XLS = 1;
    public static final int FORMAT_CSV = 10;

    private HashMap<Integer, Akzession> akzessionHashMap;
    private HashMap<Integer, Passport>  passportHashMapt;


    public Writer(String s) {
        writeResult(s, FORMAT_XLS);
    }

    public Writer(String s, int format) {
        writeResult(s, format);
    }

    public void writeResult(String filePath) {
        writeResult(filePath, FORMAT_XLS);
    }

    public void writeResult(String filePath, int format) {
        try {
            //String filePath = mFile.replaceFirst("/in/", "/" + folder + "/");

            AsyncTask execute = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    WartenDialog wd = new WartenDialog(BoniturSafe.BON_ACTIVITY, WartenDialog.MELDUNG_SPEICHERN);

                    HSSFWorkbook workbook = new HSSFWorkbook();
                    workbook = ((Writer) params[0]).addReportFinal(workbook);
                    if ((int) params[2] == FORMAT_XLS) {
                        ExcelLib.writeWorkbook(workbook, ((String) params[1]));
                    }
                    if ((int) params[2] == FORMAT_CSV) {
                        ExcelLib.writeCSV(workbook, ((String) params[1]));
                    }

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

            execute.execute(this, filePath, format);

        } catch (Exception e) {
            new ErrorLog(e, null);
            e.printStackTrace();
        }
    }

    private HSSFWorkbook addReportFinal(HSSFWorkbook workbook) {
        this.akzessionHashMap = new HashMap<Integer, Akzession>();
        this.passportHashMapt = new HashMap<Integer, Passport>();
        try {
            HSSFSheet sheet = workbook.createSheet(WORKSHEET_DATA_NAME);
            sheet.createFreezePane(13, 1);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BoniturSafe.APP_CONTEXT);
            boolean           show_datum  = preferences.getBoolean(Config.NAME_EXCEL_DATUM, Config.SHOW_EXCEL_DATUM);

            //Überschriften erstellen
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_plot));
            row.createCell(1).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_row));
            row.createCell(2).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_plant));
            row.createCell(3).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_acc_num));
            row.createCell(4).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_acc_name));
            row.createCell(5).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_var_name));
            row.createCell(6).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_var_num));
            row.createCell(7).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_genotype));
            row.createCell(8).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_col_no));
            row.createCell(9).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_mother));
            row.createCell(10).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_father));
            row.createCell(11).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_free_field));
            row.createCell(12).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_loc_info));
            row.createCell(13).setCellValue(BoniturSafe.BON_ACTIVITY.getString(R.string.excel_title_char_merk));
            row.createCell(13).setCellValue("DB Key");

            Marker[]   marker    = MarkerManager.getAllMarker();
            Standort[] standorte = StandortManager.getAllStandorte();

            int colum_offset = 13;

            int p = 0;
            for (Marker m : marker) {
                if (show_datum) {
                    row.createCell(2 * p + colum_offset).setCellValue(m.code);
                    row.createCell(2 * p + colum_offset + 1).setCellValue("");
                } else {
                    row.createCell(p + colum_offset).setCellValue(m.code);
                }
                p++;
            }
            //ENDE: Überschriften erstellen

            //Werte einfügen
            int r = 1;

            String sql = "SELECT\n" +
                    "\tstandort._id standortId,\n" +
                    "\tmarker._id markerId,\n" +
                    "\tversuchWert.wert_int,\n" +
                    "\tversuchWert.wert_datum,\n" +
                    "\tversuchWert.wert_text,\n" +
                    "\tgroup_concat(markerWert.value) markerValues\n" +
                    "FROM standort \n" +
                    "LEFT JOIN versuchWert ON standort.\"_id\" = versuchWert.standortId\n" +
                    "LEFT JOIN marker ON marker.\"_id\" = versuchWert.markerId\n" +
                    "LEFT JOIN markerWert on versuchWert.wert_id = markerWert.\"_id\"\n" +
                    "WHERE standort.versuchId = ?\n" +
                    "\tAND marker.\"_id\" is not null\n" +
                    "GROUP BY standort._id, marker._id\n" +
                    "ORDER BY standort.parzelle ASC, CAST(standort.reihe AS INTEGER) ASC, CAST(standort.pflanze AS INTEGER) ASC, marker._id";

            Cursor werteAll = BoniturSafe.db.rawQuery(sql, new String[]{"" + BoniturSafe.VERSUCH_ID});
            werteAll.moveToFirst();


            for (Standort standort : standorte) {

                Akzession akzession = null;
                Passport  passport  = null;

                if (standort.akzessionId != - 1 && standort.akzessionId != 0)
                    akzession = standort.akzession;//getAkzession(standort.akzessionId); //Akzession.findByPk(standort.akzessionId);
                if (standort.passportId != - 1 && standort.passportId != 0)
                    passport = standort.passport; //getPassport(standort.passportId); //Passport.findByPk(standort.passportId);

                row = sheet.createRow(r);
                row.createCell(0).setCellValue(standort.parzelle);
                row.createCell(1).setCellValue(standort.reihe);
                row.createCell(2).setCellValue(standort.pflanze);
                row.createCell(3).setCellValue(akzession != null ? akzession.name : "");
                row.createCell(4).setCellValue(akzession != null ? akzession.nummer : "");
                row.createCell(5).setCellValue(passport != null ? passport.leitname : "");
                row.createCell(6).setCellValue(passport != null ? passport.kennNr : "");
                row.createCell(7).setCellValue(standort.sorte);
                row.createCell(8).setCellValue(standort.sortimentsnummer);
                row.createCell(9).setCellValue(standort.mutter);
                row.createCell(10).setCellValue(standort.vater);
                row.createCell(11).setCellValue(standort.freifeld);
                row.createCell(12).setCellValue(standort.info != null ? standort.info : "");
                row.createCell(13).setCellValue(akzession != null ? akzession.merkmale : "");
                row.createCell(14).setCellValue(standort.dbKey != null ? standort.dbKey : "");


                int mp = 0;
                if(!werteAll.isClosed() && standort.id == werteAll.getInt(0)) {
                    for (Marker m : marker) {
                        if (standort.id != werteAll.getInt(0)) {
                            break;
                        }
                        if (m.id == werteAll.getInt(1)) {
                            if (show_datum) {
                                row.createCell(2 * mp + colum_offset).setCellValue(getValueFromWerteAll(werteAll, m));
                                row.createCell(2 * mp + colum_offset + 1).setCellValue(werteAll.getString(3));
                            } else {
                                row.createCell(mp + colum_offset).setCellValue(getValueFromWerteAll(werteAll, m));
                            }
                            if (!werteAll.moveToNext()){
                                werteAll.close();
                                break;
                            }
                        } else {
                            if (show_datum) {
                                row.createCell(2 * mp + colum_offset).setCellValue("");
                                row.createCell(2 * mp + colum_offset + 1).setCellValue("");
                            } else {
                                row.createCell(mp + colum_offset).setCellValue("");
                            }
                        }
                        mp++;
                    }
                }
                r++;
                if (r % 25 == 0) {
                    try {
                        //System.gc();
                    } catch (Exception e) {
                        new ErrorLog(e, BoniturSafe.APP_CONTEXT);
                    }
                }
            }
        } catch (Exception e) {
            new ErrorLog(e, BoniturSafe.APP_CONTEXT);
        }

        this.akzessionHashMap.clear();
        this.passportHashMapt.clear();

        return workbook;
    }

    private String getValueFromWerteAll(Cursor werteAll, Marker m) {
        String value = "";
        switch (m.type) {
            case Marker.MARKER_TYPE_BONITUR:
                value = werteAll.getString(5);
                break;
            case Marker.MARKER_TYPE_MESSEN:
                value = "" + werteAll.getInt(2);
                break;
            case Marker.MARKER_TYPE_DATUM:
            case Marker.MARKER_TYPE_BEMERKUNG:
            case Marker.MARKER_TYPE_BBCH:
                value = werteAll.getString(4);
                break;
            case Marker.MARKER_TYPE_NUMERIC:
                value = "" + werteAll.getDouble(
                    werteAll.getColumnIndex(VersuchWert.COLUMN_WERT_NUMERIC)
                );
        }
        value = value.replace("\n", "").replace("\r", "");
        if (value.isEmpty()) {
            return "";
        }
        return value;
    }

    private Akzession getAkzession(int id) {
        Akzession res = null;
        if (this.akzessionHashMap.containsKey(id)) {
            res = Akzession.findByPk(id);
            this.akzessionHashMap.put(id, res);
        } else {
            res = this.akzessionHashMap.get(id);
        }
        return res;
    }

    private Passport getPassport(int id) {
        Passport res = null;
        if (this.passportHashMapt.containsKey(id)) {
            res = Passport.findByPk(id);
            this.passportHashMapt.put(id, res);
        } else {
            res = this.passportHashMapt.get(id);
        }
        return res;
    }
}
