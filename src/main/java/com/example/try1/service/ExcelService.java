package com.example.try1.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.example.try1.model.Product;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    // Метод для збереження продуктів в Excel
    public File saveProductsToExcel(List<Product> products, float currency) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Продукти");


        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Назва");
        header.createCell(1).setCellValue("Ціна в гривнях");
        header.createCell(2).setCellValue("Ціна в долларах");
        header.createCell(3).setCellValue("Зображення");
        header.createCell(4).setCellValue("Посилання");


        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getTitle());
            row.createCell(1).setCellValue(product.getPrice());
            float dollarPrice = product.getPrice() / currency;
            row.createCell(2).setCellValue(dollarPrice);
            row.createCell(3).setCellValue(product.getImageUrl());
            row.createCell(4).setCellValue(product.getProductUrl());
        }

        //Підгонка розміру
        for (int i = 0; i < 5; i++) { // 5 столбцов
            sheet.autoSizeColumn(i);
        }

        // Зберігаємо файл у тимчасовій папці
        File file = new File(System.getProperty("java.io.tmpdir") + "products.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            workbook.close();
            System.out.println("Дані збережені в " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}