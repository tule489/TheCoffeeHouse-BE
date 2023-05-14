package com.theCoffeeHouse.theCoffeeHouse.Repositories;

import com.theCoffeeHouse.theCoffeeHouse.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByDetailedCategoryId(Long detailedCategoryId);
    @Modifying
    @Transactional
    @Query("Update Product product set product.isHotProduct = '1' where product.id = :productId")
    void setTrueHotProduct(@Param("productId") Long productId);
    @Modifying
    @Transactional
    @Query("Update Product product set product.isHotProduct = '0' where product.id = :productId")
    void setFalseHotProduct(@Param("productId") Long productId);
    @Modifying
    @Transactional
    @Query(value = "Select p from Product p where p.isHotProduct = '1'")
    List<Product> getHotProduct();
}
