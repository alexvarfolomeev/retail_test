package ru.varfolomeev.retail_test.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static java.util.Objects.isNull;

@Component
public class Utils {
    public static Map<Integer, List<String>> readFromExcel(MultipartFile file, String sheetName) throws IOException, InvalidFormatException {
        Map<Integer, List<String>> data = new HashMap<>();
        Workbook workbook = new XSSFWorkbook(convertMultipartToFile(file));
        Sheet sheet = workbook.getSheet(sheetName);
        int rows = sheet.getPhysicalNumberOfRows();
        int columns = sheet.getRow(0).getLastCellNum();
        for (int i = 1; i < rows; i++) {
            data.put(i, new ArrayList<>());
            var row = sheet.getRow(i);
            for (int j = 0; j < columns; j++) {
                var cell = row.getCell(j);
                if (!isNull(cell)){
                    switch (cell.getCellType()){
                        case NUMERIC -> data.get(i).add(String.valueOf(cell.getNumericCellValue()));
                        case STRING -> data.get(i).add(cell.getStringCellValue());
                    }
                } else {
                    break;
                }
            }
        }
        workbook.close();
        return data;
    }

    public static void writeToExcel(){

    }

    public static File convertMultipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File(Objects.requireNonNull(multipart.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipart.getBytes());
        fos.close();
        return convFile;
    }
}
