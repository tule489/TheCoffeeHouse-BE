package com.theCoffeeHouse.theCoffeeHouse.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "tbProduct")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true, length = 3000)
    private String name;
    private Long categoryId;
    private String price;
    @Column(columnDefinition = "LONGTEXT")
    private String image;
    private String description;

    public Product() {
    }

    public Product(String name, Long categoryId, String price, String image, String description) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.image = image;
        this.description = description;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
