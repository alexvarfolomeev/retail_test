package ru.varfolomeev.retail_test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.varfolomeev.retail_test.service.ShipmentService;

import java.util.List;

@RestController
@RequestMapping("api/v1/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final ShipmentService service;

    @GetMapping("/promo-analysis")
    public ResponseEntity<?> getPromoAnalysis() {
        return ResponseEntity.ok(service.getAnalysisWithPromoSign());
    }

    @PostMapping("/promo-analysis/filter/chain-product")
    private ResponseEntity<?> getAnalysisWithChainsAndProductsFilter(@RequestParam List<Integer> chains, @RequestParam List<Integer> products) {
        return ResponseEntity.ok(service.getAnalysisWithDaysChainsAndProductsFilter(chains, products));
    }
}
