package com.example.try1.service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class DatabaseHandler {
    private static final String JDBC_URL = "jdbc:h2:./database";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public void processExcelFile(File file) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(0);
            String tableName = sheet.getSheetName();

            List<String> headers = extractHeaders(sheet);
            createTable(connection, tableName, headers);
            insertData(connection, sheet, tableName, headers);

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> extractHeaders(Sheet sheet) {
        List<String> headers = new ArrayList<>();
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                if (cell.getCellType() == CellType.STRING) {
                    headers.add(cell.getStringCellValue());
                } else {
                    break;
                }
            }
        }
        return headers;
    }

    private void createTable(Connection connection, String tableName, List<String> headers) throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

        for (String header : headers) {
            String columnName = header.replaceAll("\\s+", "_");
            sql.append(columnName).append(" TEXT, ");
        }

        sql.setLength(sql.length() - 2);
        sql.append(")");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql.toString());
        }
    }

    private void insertData(Connection connection, Sheet sheet, String tableName, List<String> headers) throws SQLException {

        List<String> formattedHeaders = new ArrayList<>();
        for (String header : headers) {
            formattedHeaders.add(header.replaceAll("\\s+", "_"));
        }

        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        sql.append(String.join(", ", formattedHeaders)).append(") VALUES (" + "?, ".repeat(formattedHeaders.size()));
        sql.setLength(sql.length() - 2);
        sql.append(")");

        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                for (int j = 0; j < formattedHeaders.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    pstmt.setString(j + 1, cell.toString());
                }
                pstmt.executeUpdate();
            }
        }
    }
}