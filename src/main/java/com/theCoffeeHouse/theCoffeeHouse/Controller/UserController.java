package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Models.User;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository repository;

    @GetMapping("/getAll")
    List<User> getAllProducts() {
        return repository.getAllAccount();
    }

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

    @PostMapping("/changePassword")
    ResponseEntity<ResponseObject> changePassword(@RequestParam Long userId, @RequestParam String newPassword) {
        if(repository.existsById(userId)) {
            repository.changePassword(userId, newPassword);
            return ResponseEntity.status((HttpStatus.OK)).body(
                    new ResponseObject("ok", "Change password successfully", "")
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Can not find user id", "")
        );
    }

    @PutMapping("/deleteMultiple")
    ResponseEntity<ResponseObject> deleteMultipleUser(@RequestBody String[] arrayId) {
        int count = 0;

        if (arrayId.length < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Delete id is empty!", "")
            );
        }
        for (String id : arrayId) {
            if (repository.existsById(Long.parseLong(id))) {
                repository.deleteById(Long.parseLong(id));
                count++;
            }
        }
        if (count == arrayId.length) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted user successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find user to delete", "")
        );
    }
}
