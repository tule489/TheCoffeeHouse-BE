package com.theCoffeeHouse.theCoffeeHouse.Models;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "tbProduct")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 3000)
    private String name;
    private Long detailedCategoryId;
    private String price;
    @Column(columnDefinition = "LONGTEXT")
    private String image;
    private String description;
    @Column(nullable = false)
    private String isHotProduct = "0";
    public Product() {
    }

    public Product(String name, Long detailedCategoryId, String price, String image, String description) {
        this.name = name;
        this.detailedCategoryId = detailedCategoryId;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public String getIsHotProduct() {
        return isHotProduct;
    }

    public void setIsHotProduct(String isHotProduct) {
        this.isHotProduct = isHotProduct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDetailedCategoryId() {
        return detailedCategoryId;
    }

    public void setDetailedCategoryId(Long detailedCategoryId) {
        this.detailedCategoryId = detailedCategoryId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
