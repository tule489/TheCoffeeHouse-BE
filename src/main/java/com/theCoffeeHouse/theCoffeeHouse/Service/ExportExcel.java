package com.theCoffeeHouse.theCoffeeHouse.Service;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.theCoffeeHouse.theCoffeeHouse.Models.OrderByDay;
import com.theCoffeeHouse.theCoffeeHouse.Models.OrderByMonth;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.OrderRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

public class ExportExcel {
    private final XSSFWorkbook workbook;
    private OrderRepository repository;

    public ExportExcel(OrderRepository repository) {
        this.repository = repository;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine(XSSFSheet sheet, String month) {
        Row row = sheet.createRow(1);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeight(13);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);

        CellStyle styleForHeader = workbook.createCellStyle();
        XSSFFont fontForHeader = workbook.createFont();
        fontForHeader.setFontHeight(16);
        fontForHeader.setFontName("Times New Roman");
        fontForHeader.setBold(true);
        styleForHeader.setFont(fontForHeader);

        createCell(sheet.createRow(0), 0, "Báo cáo doanh thu tháng " + month, styleForHeader, sheet);
        createCell(row, 0, "Ngày", style, sheet);
        createCell(row, 1, "Tổng tiền", style, sheet);
    }

    private void writeHeaderLineForYear(XSSFSheet sheet) {
        Row row = sheet.createRow(1);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeight(13);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);

        CellStyle styleForHeader = workbook.createCellStyle();
        XSSFFont fontForHeader = workbook.createFont();
        fontForHeader.setFontHeight(16);
        fontForHeader.setFontName("Times New Roman");
        fontForHeader.setBold(true);
        styleForHeader.setFont(fontForHeader);

        createCell(sheet.createRow(0), 0, "Báo cáo doanh thu năm " + LocalDate.now().getYear(), styleForHeader, sheet);
        createCell(row, 0, "Tháng", style, sheet);
        createCell(row, 1, "Tổng tiền", style, sheet);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style, XSSFSheet sheet) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue(String.valueOf(value));
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(XSSFSheet sheet, List<OrderByDay> listOrderByDay) {
        int rowCount = 2;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        font.setBold(false);
        font.setFontName("Times New Roman");

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        for (OrderByDay value : listOrderByDay) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, value.getDay(), style, sheet);
            createCell(row, columnCount++, value.getTotalMoney(), style, sheet);

        }
    }

    private void writeDataLinesForYear(XSSFSheet sheet, List<OrderByMonth> listOrderByMonth) {
        int rowCount = 2;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        font.setBold(false);
        font.setFontName("Times New Roman");

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        for (OrderByMonth value : listOrderByMonth) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, value.getMonth(), style, sheet);
            createCell(row, columnCount++, value.getTotalMoney(), style, sheet);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        XSSFSheet sheetYear = workbook.createSheet("Doanh thu năm " + LocalDate.now().getYear());
        writeHeaderLineForYear(sheetYear);
        Object[] objectsOrderByMonth = repository.getOrderByMonth(String.valueOf(LocalDate.now().getYear()));
        List<OrderByMonth> orderByMonths = Arrays.stream(objectsOrderByMonth).map(
                data -> {
                    Object[] arr = (Object[]) data;
                    return new OrderByMonth((String) arr[0], ((BigDecimal) arr[1]).doubleValue());
                }
        ).toList();
        writeDataLinesForYear(sheetYear, orderByMonths);
        for(int i = 1; i <= LocalDate.now().getMonthValue(); i++) {
            XSSFSheet sheet = workbook.createSheet("Tháng " + i);
            writeHeaderLine(sheet, String.valueOf(i));
            Object[] objects = repository.getOrderByDay(String.valueOf(i));
            List<OrderByDay> orderByDays = Arrays.stream(objects).map(
                    data -> {
                        Object[] arr = (Object[]) data;
                        return new OrderByDay((String) arr[0], ((BigDecimal) arr[1]).doubleValue());
                    }
            ).toList();
            writeDataLines(sheet, orderByDays);
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
