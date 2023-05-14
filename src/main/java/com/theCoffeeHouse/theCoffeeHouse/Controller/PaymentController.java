package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.Order;
import com.theCoffeeHouse.theCoffeeHouse.Models.OrderDetails;
import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.OrderDetailsRepository;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(path = "/api/v1/orders")
@CrossOrigin
public class PaymentController {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @GetMapping("/getAll")
    List<Order> getAllOrders() {
        return repository.findAll();
    }

    @PostMapping("/addOrder")
    ResponseEntity<ResponseObject> addOrder(@RequestBody Order newOrder) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add a new order successfully", repository.save((newOrder)))
        );
    }

    @PostMapping("/addOrderDetails")
    ResponseEntity<ResponseObject> addOrderDetails(@RequestBody String[] arrayProductId, @RequestParam String orderId, HttpServletRequest request) {
        int count = 0;

        if (arrayProductId.length < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Delete id is empty!", "")
            );
        }
        for (String productId : arrayProductId) {
            orderDetailsRepository.save(new OrderDetails(Long.parseLong(orderId), Long.parseLong(productId), request.getHeader("User-Agent")));
        }
        if (count == arrayProductId.length) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Add order details successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Error system", "")
        );
    }

    @PutMapping("/deleteMultiple")
    ResponseEntity<ResponseObject> deleteMultipleProduct(@RequestBody String[] arrayId) {
        int count = 0;

        if (arrayId.length < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Delete id is empty!", "")
            );
        }
        for (String id : arrayId) {
            if (repository.existsById(Long.parseLong(id))) {
                repository.deleteById(Long.parseLong(id));
                count++;
            }
        }
        if (count == arrayId.length) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted order successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find order to delete", "")
        );
    }

    @PutMapping("/updateStatus/{orderId}")
    ResponseEntity<ResponseObject> updateStatus(@PathVariable Long orderId, @RequestBody String newStatus) {
        Order foundOrder = repository.findById(orderId).orElseGet(() -> {
            return new Order();
        });
        if(foundOrder.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Order id is not exist ", "" )
            );
        }
        repository.updateStatus(newStatus.substring(1, newStatus.length() -1), orderId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update status successfully", "" )
        );
    }
}
