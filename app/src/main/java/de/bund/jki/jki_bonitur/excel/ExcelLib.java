package de.bund.jki.jki_bonitur.excel;

/**
 * Created by Toni on 11.05.2015.
 */

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Toni on 03.03.2015.
 */
public class ExcelLib {

    public static HSSFWorkbook openExcelFile(String path) throws IOException {
        File inputWorkbook = new File(path);
        if (! inputWorkbook.exists())
            inputWorkbook.createNewFile();
        FileInputStream fileInputStream = new FileInputStream(inputWorkbook);

        HSSFWorkbook result = new HSSFWorkbook(fileInputStream);
        fileInputStream.close();

        return result;
    }

    public static String getCellValue(HSSFSheet sheet, int row, int cell) {
        return sheet.getRow(row).getCell(cell).toString();
    }

    public static void writeWorkbook(HSSFWorkbook workbook, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            int a = 1;
        }
    }

    public static boolean isCellEmpty(Row row, int rowNumb) {
        if (row.getCell(rowNumb) == null)
            return true;

        return row.getCell(rowNumb).getCellType() == 3;

        /*
        if(row.getCell(rowNumb).getStringCellValue().isEmpty())
            return true;

        else
            return false;
        */
    }

    public static String getCellValueString(Row row, int rowNumb) {
        return getCellValueString(row, rowNumb, false);
    }

    public static String getCellValueString(Row row, int rowNumb, boolean asInt) {
        if (row.getLastCellNum() < rowNumb) return null;

        if (row.getCell(rowNumb) == null) return null;

        try {
            switch (row.getCell(rowNumb).getCellType()) {
                case 0:
                    if (asInt)
                        return "" + (int) row.getCell(rowNumb).getNumericCellValue();
                    else
                        return "" + row.getCell(rowNumb).getNumericCellValue();
                case 1:
                    return "" + row.getCell(rowNumb).getStringCellValue();
                case 3:
                    return "";
                default:
                    return "";
            }
        } catch (Exception e) {
            return null;
        }
    }


}
