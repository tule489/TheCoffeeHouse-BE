package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.OrderByDay;
import com.theCoffeeHouse.theCoffeeHouse.Models.OrderByMonth;
import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.OrderRepository;
import com.theCoffeeHouse.theCoffeeHouse.Service.ExportExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/report")
@CrossOrigin
public class ReportController {
    @Autowired
    private OrderRepository repository;

    @PostMapping("/export/excel")
    public void exportOrderByDayToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(dateFormatter);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Bao_cao_doanh_thu_" + formattedDate + ".xlsx";
        response.setHeader(headerKey, headerValue);
//        Object[] objects = repository.getOrderByDay(month);
//        List<OrderByDay> orderByDays = Arrays.stream(objects).map(
//                data -> {
//                    Object[] arr = (Object[]) data;
//                    return new OrderByDay((String) arr[0], ((BigDecimal) arr[1]).doubleValue());
//                }
//        ).toList();

        ExportExcel excelExporter = new ExportExcel(repository);
        excelExporter.export(response);
    }

    @GetMapping("/getOrderByDay/{month}")
    ResponseEntity<ResponseObject> getOrderByDay(@PathVariable String month) {
        Object[] objects = repository.getOrderByDay(month);
        List<OrderByDay> orderByDays = Arrays.stream(objects).map(
                data -> {
                    Object[] arr = (Object[]) data;
                    return new OrderByDay((String) arr[0], ((BigDecimal) arr[1]).doubleValue());
                }
        ).toList();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get data successfully", orderByDays)
        );
    }

    @GetMapping("/getOrderByMonth/{year}")
    ResponseEntity<ResponseObject> getOrderByMonth(@PathVariable String year) {
        Object[] objects = repository.getOrderByMonth(year);
        List<OrderByMonth> orderByMonths = Arrays.stream(objects).map(
                data -> {
                    Object[] arr = (Object[]) data;
                    return new OrderByMonth((String) arr[0], ((BigDecimal) arr[1]).doubleValue());
                }
        ).toList();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get data successfully", orderByMonths)
        );
    }
}
