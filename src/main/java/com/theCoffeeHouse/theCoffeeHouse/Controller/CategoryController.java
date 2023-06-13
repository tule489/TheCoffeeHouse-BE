package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.Category;
import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.CategoryRepository;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.DetailedCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping(path = "/api/v1/categories")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryRepository repository;
    @Autowired
    private DetailedCategoryRepository detailedCategoryRepository;

    @GetMapping("/getAll")
    List<Category> getAllCategories() {
        return repository.findAll();
    }

    @GetMapping("/getById/{id}")
    ResponseEntity<ResponseObject> getCategoryById(@PathVariable Long id) {
        Optional<Category> foundCategory = repository.findById(id);
        return foundCategory.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query category successfully", foundCategory)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find category with id = " + id, "")
                );
    }

    @PostMapping("/add")
    ResponseEntity<ResponseObject> addCategory(@RequestBody Category newCategory) {
        List<Category> foundCategory = repository.findByName(newCategory.getName().trim());
        if (foundCategory.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Category name already exist", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add a new category successfully", repository.save((newCategory)))
        );
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updateCategory(@RequestBody Category newCategory, @PathVariable Long id) {
        List<Category> foundCategories = repository.findByName(newCategory.getName().trim());
        if (foundCategories.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Category name already exist", "")
            );
        }
        Category updateCategory = repository.findById(id).map(category -> {
            category.setName(newCategory.getName());
            return repository.save(category);

        }).orElseGet(() -> {
            newCategory.setId(id);
            return repository.save(newCategory);
        });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Updated Category successfully", updateCategory)
        );
    }

    @DeleteMapping("/deleteById/{id}")
    ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted category successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find category to delete", "")
        );
    }

    @PostMapping("/deleteMultiple")
    ResponseEntity<ResponseObject> deleteMultipleProduct(@RequestBody List<String> arrayId) {
        int count = 0;
        if (arrayId.size() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Delete id is empty!", "")
            );
        }
        if (detailedCategoryRepository.findAll().size() > 0) {
            for (String id : arrayId) {
                if (detailedCategoryRepository.findByCategoryId(Long.parseLong(id)).size() > 0) {
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
        if (count == arrayId.size()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted category successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find category to delete", "")
        );
    }

}
