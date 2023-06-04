package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.OrderByDay;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.OrderRepository;
import com.theCoffeeHouse.theCoffeeHouse.Service.ExportExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/report")
@CrossOrigin
public class ReportController {
    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("/export/excelByMonth/{month}")
    public void exportOrderByDayToExcel(HttpServletResponse response, @PathVariable String month) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        Object[] objects = orderRepository.getOrderByDay(month);
        List<OrderByDay> orderByDays = Arrays.stream(objects).map(
                data -> {
                    Object[] arr = (Object[]) data;
                    return new OrderByDay((String) arr[0], ((BigDecimal) arr[1]).doubleValue());
                }
        ).toList();
        ExportExcel excelExporter = new ExportExcel(orderByDays);

        excelExporter.export(response);
    }
}
