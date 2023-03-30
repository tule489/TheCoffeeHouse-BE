package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Models.User;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/v1/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    ResponseEntity<ResponseObject> getUserByUsername(@RequestBody User user) {
        List<User> foundUser = repository.findByUsername(user.getUsername());
        if (foundUser.size() > 0) {
            if (user.getPassword().compareTo(foundUser.get(0).getPassword()) == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Login successfully", foundUser.get(0)));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ResponseObject("failed", "Your password is incorrect", ""));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "Username is not exits", "")
        );
    }

    @RequestMapping("/register")
    ResponseEntity<ResponseObject> registerAccount(@RequestBody User newUser) {
        List<User> foundUser = repository.findByUsername(newUser.getUsername().trim());
        if (foundUser.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Username already exist", ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Register successfully", repository.save((newUser)))
        );
    }

    @RequestMapping("/authorization/{userId}")
    ResponseEntity<ResponseObject> checkingAuthorization(@PathVariable Long userId) {
        Optional<User> foundUser = repository.findById(userId);
        if (foundUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get permission successfully", foundUser.get().getPermission()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "User id is not exist", "")
        );
    }
}
