package com.theCoffeeHouse.theCoffeeHouse.Repositories;

import com.theCoffeeHouse.theCoffeeHouse.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
    @Modifying
    @Transactional
    @Query("Update User user set user.password = :newPassword where user.id = :userId")
    void changePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    @Modifying
    @Transactional
    @Query("Select user from User user where user.username != 'admin'")
    List<User> getAllAccount();
}
