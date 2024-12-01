package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {
    private static final String FILE_PATH = "D:\\uitestexcel.xlsx";

    public static String[] getTestData(String firstName, String lastName, String postalCode) {
        String[] data = new String[3];
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0); 
            for (Row row : sheet) {
                if (row.getCell(0).getStringCellValue().equals(firstName) &&
                    row.getCell(1).getStringCellValue().equals(lastName) &&
                    row.getCell(2).getStringCellValue().equals(postalCode)) {
                    data[0] = row.getCell(0).getStringCellValue();
                    data[1] = row.getCell(1).getStringCellValue(); 
                    data[2] = row.getCell(2).getStringCellValue(); 
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}