package com.employeestracker.util;

import com.employeestracker.exception.ReportException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Component
@Slf4j
public class ExcelGenerator<T> {

    public byte[] generate(List<T> data, Class<T> clazz) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(clazz.getSimpleName());
        sheet.setDefaultColumnWidth(15);
        Row headerRow = sheet.createRow(0);
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Cell cell = headerRow.createCell(i);
            fields[i].setAccessible(true);
            cell.setCellValue(fields[i].getName());
        }

        int rowNum = 1;
        for (T object : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < fields.length; i++) {
                Cell cell = row.createCell(i);
                try {
                    Object value = fields[i].get(object);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                } catch (IllegalAccessException e) {
                    log.error("An error occurred while accessing the field: " + e);
                }
            }
        }

        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
            workbook.write(byteArray);
            return byteArray.toByteArray();
        } catch (IOException e) {
            log.error("Error while processing report: ", e);
            throw new ReportException(e.getMessage());
        }
    }
}