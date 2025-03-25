package com.example.try1.controller;

import com.example.try1.model.Product;
import com.example.try1.service.CurrencyService;
import com.example.try1.service.ExcelService;
import com.example.try1.service.ProductParserService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductParserService parserService;
    private final CurrencyService currencyService;
    private final ExcelService excelService;

    public ProductController(ProductParserService parserService, CurrencyService currencyService, ExcelService excelService) {
        this.parserService = parserService;
        this.currencyService = currencyService;
        this.excelService = excelService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return parserService.getProducts();
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadExcel() throws IOException {
        List<Product> products = parserService.getProducts();
        float usdRate = currencyService.getUsdRate();
        File file = excelService.saveProductsToExcel(products, usdRate);
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}