package ru.varfolomeev.retail_test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.varfolomeev.retail_test.model.Price;
import ru.varfolomeev.retail_test.service.PriceService;

import java.util.Collection;

@RestController
@RequestMapping("api/v1/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final PriceService priceService;

    @GetMapping("/get-price/{id}")
    public ResponseEntity<Price> getPrice(@PathVariable Long id){
        return ResponseEntity.ok(priceService.getPrice(id));
    }

    @GetMapping("/get-all-prices")
    public ResponseEntity<Collection<Price>> getAllPrices(){
        return ResponseEntity.ok(priceService.getAllPrices());
    }

    @PostMapping("/update")
    public ResponseEntity<Price> updatePrice(@RequestBody Price price){
        return ResponseEntity.ok(priceService.updatePrice(price));
    }

    @PostMapping("/save")
    private ResponseEntity<?> savePrice(@RequestBody Price price){
        priceService.savePrice(price);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{id}")
    private ResponseEntity<?> deletePrice(@PathVariable Long id){
        priceService.deletePrice(id);
        return ResponseEntity.ok().build();
    }
}
