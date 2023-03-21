package com.theCoffeeHouse.theCoffeeHouse.Repositories;

import com.theCoffeeHouse.theCoffeeHouse.Models.Category;
import com.theCoffeeHouse.theCoffeeHouse.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);
}
