package ru.varfolomeev.retail_test.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileOperations {
    void saveDataFromExcel(MultipartFile file, String sheetName) throws IOException, InvalidFormatException;
}
