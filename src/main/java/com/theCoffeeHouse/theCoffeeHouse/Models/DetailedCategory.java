package com.theCoffeeHouse.theCoffeeHouse.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "tbDetailedCategory")
public class DetailedCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long categoryId;
    @Column(nullable = false, unique = true, length = 3000)
    private String name;

    public DetailedCategory() {
    }

    public DetailedCategory(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
