package ru.varfolomeev.retail_test.controller;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.varfolomeev.retail_test.service.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final PriceService priceService;
    private final ShipmentService shipmentService;
    private final ProductService productService;
    private final CustomerService customerService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDataFromExcel(@RequestBody MultipartFile excelFile, @RequestParam String sheetName) {
        try {
            getActualService(sheetName).saveDataFromExcel(excelFile, sheetName);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Data uploaded to database successfully");
    }

    private FileOperations getActualService(String sheet) {
        switch (sheet) {
            case "Actuals" -> {
                return shipmentService;
            }
            case "Price" -> {
                return priceService;
            }
            case "Products" -> {
                return productService;
            }
            case "Customers" -> {
                return customerService;
            }
        }
        return null;
    }
}
