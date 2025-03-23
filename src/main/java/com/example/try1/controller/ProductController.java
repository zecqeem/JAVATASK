package com.example.try1.controller;

import com.example.try1.DatabaseHandler;
import com.example.try1.service.CurrencyService;
import com.example.try1.service.ExcelService;
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
import com.example.try1.service.ProductParserService;
import com.example.try1.model.Product;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductParserService parserService;

    public ProductController(ProductParserService parserService) {
        this.parserService = parserService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return parserService.getProducts(); // Отримати продукти
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadExcel() throws IOException {
        // Генеруємо файл і зберігаємо його в тимчасовій папці
        ProductParserService productParserService = new ProductParserService();
        List<Product> products = productParserService.getProducts();
        CurrencyService currencyService = new CurrencyService();
        ExcelService excelService = new ExcelService();
        float usdRate = currencyService.getUsdRate();
        File file = excelService.saveProductsToExcel(products, usdRate);

        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.processExcelFile(file); // Обробляємо файл з продуктами і зберігаємо в БД

        Resource resource = new FileSystemResource(file);

        // Відправляємо файл на скачування
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/")
    public String index() {
        return "index"; // Файл index.html в папці templates
    }
}