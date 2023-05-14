package com.theCoffeeHouse.theCoffeeHouse.Repositories;

import com.theCoffeeHouse.theCoffeeHouse.Models.DetailedCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailedCategoryRepository extends JpaRepository<DetailedCategory, Long> {
    List<DetailedCategory> findByName(String name);
    List<DetailedCategory> findByCategoryId(Long categoryId);
}
