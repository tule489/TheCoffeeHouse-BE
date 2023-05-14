package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.Product;
import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping("/getAll")
    List<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/getById/{id}")
    ResponseEntity<ResponseObject> getProductById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query product successfully", foundProduct)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find product with id = " + id, "")
                );
    }

    @PostMapping("/add")
    ResponseEntity<ResponseObject> addProduct(@RequestBody Product newProduct) {
        List<Product> foundProducts = repository.findByName(newProduct.getName().trim());
        if (foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already exist", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add a new product successfully", repository.save((newProduct)))
        );
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        List<Product> foundProducts = repository.findByName(newProduct.getName().trim());
        Product foundProduct = repository.findById(id).orElseGet(() -> {
            return new Product("", 0L, "", "", "");
        });
        if (foundProducts.size() > 0) {
            if (foundProducts.get(0).getId() != id && newProduct.getName().trim().compareTo(foundProduct.getName()) == 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Product name already exist", "")
                );
            }
        }
        Product updateProduct = repository.findById(id).map(product -> {
            product.setName(newProduct.getName());
            product.setDetailedCategoryId(newProduct.getDetailedCategoryId());
            product.setPrice(newProduct.getPrice());
            product.setImage(newProduct.getImage());
            product.setDescription(newProduct.getDescription());
            return repository.save(product);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return repository.save(newProduct);
        });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Updated product successfully", updateProduct)
        );
    }

    @DeleteMapping("/deleteById/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted product successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find product to delete", "")
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
                    new ResponseObject("ok", "Deleted product successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find product to delete", "")
        );
    }

    @PostMapping("/setTrueHotProduct")
    ResponseEntity<ResponseObject> setTrueHotProduct(@RequestBody String[] arrayId) {
        int count = 0;

        if (arrayId.length < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Product id is empty!", "")
            );
        }
        for (String id : arrayId) {
            if (repository.existsById(Long.parseLong(id))) {
                repository.setTrueHotProduct(Long.parseLong(id));
                count++;
            }
        }
        if (count == arrayId.length) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Set product successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find product", "")
        );
    }

    @PostMapping("/setFalseHotProduct")
    ResponseEntity<ResponseObject> setFalseHotProduct(@RequestBody String[] arrayId) {
        int count = 0;

        if (arrayId.length < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Product id is empty!", "")
            );
        }
        for (String id : arrayId) {
            if (repository.existsById(Long.parseLong(id))) {
                repository.setFalseHotProduct(Long.parseLong(id));
                count++;
            }
        }
        if (count == arrayId.length) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Set product successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find product", "")
        );
    }

    @GetMapping("/getHotProduct")
    List<Product> getHotProduct() {
        return repository.getHotProduct();
    }
}
