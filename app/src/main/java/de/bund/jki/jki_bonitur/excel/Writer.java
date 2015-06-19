package de.bund.jki.jki_bonitur.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;

import de.bund.jki.jki_bonitur.MarkerManager;
import de.bund.jki.jki_bonitur.StandortManager;
import de.bund.jki.jki_bonitur.db.Akzession;
import de.bund.jki.jki_bonitur.db.Marker;
import de.bund.jki.jki_bonitur.db.Passport;
import de.bund.jki.jki_bonitur.db.Standort;

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

            HSSFWorkbook workbook = new HSSFWorkbook();
            workbook = this.addReportFinal(workbook);
            ExcelLib.writeWorkbook(workbook, filePath);

        } catch (Exception e){
            e.printStackTrace();
            //ToDo: Fehlermeldung speichern Fehlgeschlagen....
        }
    }

    private HSSFWorkbook addReportFinal(HSSFWorkbook workbook)
    {
        HSSFSheet sheet = workbook.createSheet("Report");
        sheet.createFreezePane(7,0);

        //Überschriften erstellen
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Standort");
        row.createCell(1).setCellValue("Akzessionsnummer");
        row.createCell(2).setCellValue("Akzessionsname");
        row.createCell(3).setCellValue("Kennnr");
        row.createCell(4).setCellValue("Leitname");
        row.createCell(5).setCellValue("StandortInfo");
        row.createCell(6).setCellValue("Charkteristische Merkmale");

        Marker[] marker = MarkerManager.getAllMarker();
        Standort[] standorte = StandortManager.getAllStandorte();

        int p = 0;
        for(Marker m : marker){
            row.createCell(p + 7).setCellValue(m.code);
            p++;
        }
        //ENDE: Überschriften erstellen

        //Werte einfügen
        int r = 1;

        for(Standort standort: standorte) {

            Akzession akzession = null;
            Passport passport = null;

            if(standort.akzessionId != -1)
                akzession = Akzession.findByPk(standort.akzessionId);
            if(standort.passportId != -1)
                passport = Passport.findByPk(standort.passportId);

            row = sheet.createRow(r);
            row.createCell(0).setCellValue(standort.getName());
            row.createCell(1).setCellValue(akzession != null ? akzession.nummer : "");
            row.createCell(2).setCellValue(akzession != null ? akzession.name : "");
            row.createCell(3).setCellValue(passport != null ? passport.kennNr : "");
            row.createCell(4).setCellValue(passport != null ? passport.leitname : "");
            row.createCell(5).setCellValue(""); // ToDo: Standortinformationen Implementieren
            row.createCell(6).setCellValue(akzession != null ? akzession.merkmale : "");

            int mp = 0;
            for(Marker m : marker)
            {
                row.createCell(mp + 7).setCellValue(standort.getValue(m.id));
                mp++;
            }
            r++;
        }

        return workbook;
    }
}
