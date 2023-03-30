package com.theCoffeeHouse.theCoffeeHouse.Repositories;

import com.theCoffeeHouse.theCoffeeHouse.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
}
