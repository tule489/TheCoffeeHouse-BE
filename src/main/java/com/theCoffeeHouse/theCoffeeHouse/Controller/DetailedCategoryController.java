package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.DetailedCategory;
import com.theCoffeeHouse.theCoffeeHouse.Models.Product;
import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.DetailedCategoryRepository;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/detailedCategories")
@CrossOrigin
public class DetailedCategoryController {
    @Autowired
    private DetailedCategoryRepository repository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/getAll")
    List<DetailedCategory> getAllDetailedCategories() {
        return repository.findAll();
    }

    @GetMapping("/getById/{id}")
    ResponseEntity<ResponseObject> getDetailedCategoryById(@PathVariable Long id) {
        Optional<DetailedCategory> foundDetailedCategory = repository.findById(id);
        return foundDetailedCategory.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query detailed category successfully", foundDetailedCategory)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find detailed category with id = " + id, "")
                );
    }

    @PostMapping("/add")
    ResponseEntity<ResponseObject> addDetailCategory(@RequestBody DetailedCategory newDetailedCategory) {
        List<DetailedCategory> foundDetailedCategory = repository.findByName(newDetailedCategory.getName().trim());
        if (foundDetailedCategory.size() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Detailed category name already exist", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add a new detailed category successfully", repository.save((newDetailedCategory)))
        );
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updateDetailCategory(@RequestBody DetailedCategory newDetailedCategory, @PathVariable Long id) {
        List<DetailedCategory> foundDetailCategories = repository.findByName(newDetailedCategory.getName().trim());
        DetailedCategory foundDetailedCategory = repository.findById(id).orElseGet(() -> {
            return new DetailedCategory(0L, "");
        });
        if (foundDetailCategories.size() > 0) {
            if (foundDetailCategories.get(0).getId() != id && newDetailedCategory.getName().trim().compareTo(foundDetailedCategory.getName()) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("failed", "Product name already exist", "")
                );
            }
        }
        DetailedCategory updateDetailedCategory = repository.findById(id).map(detailedCategory -> {
            detailedCategory.setName(newDetailedCategory.getName());
            detailedCategory.setCategoryId(newDetailedCategory.getCategoryId());
            return repository.save(detailedCategory);
        }).orElseGet(() -> {
            newDetailedCategory.setId(id);
            return repository.save(newDetailedCategory);
        });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Updated detailed category successfully", updateDetailedCategory)
        );
    }

    @DeleteMapping("/deleteById/{id}")
    ResponseEntity<ResponseObject> deleteDetailedCategory(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted detailed category successfully", repository.findAll()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "Cannot find detailed category to delete", "")
        );
    }

    @PostMapping("/deleteMultiple")
    ResponseEntity<ResponseObject> deleteMultipleDetailedCategory(@RequestBody String[] arrayId) {
        int count = 0;
        if (arrayId.length < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Delete id is empty!", "")
            );
        }
        if (productRepository.findAll().size() > 0) {
            for (String id : arrayId) {
                if (productRepository.findByDetailedCategoryId(Long.parseLong(id)).size() > 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseObject("failed", "Exist a product reference to detailed category you want to delete", "")
                    );
                }
            }
        }
        for (String id : arrayId) {
            if (repository.existsById(Long.parseLong(id))) {
                repository.deleteById(Long.parseLong(id));
                count++;
            }
        }
        if (count == arrayId.length) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted detailed category successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find detailed category to delete", arrayId)
        );
    }
}
