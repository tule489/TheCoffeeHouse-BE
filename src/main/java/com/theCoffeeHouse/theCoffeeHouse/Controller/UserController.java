package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Models.User;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/getAll")
    List<User> getAllUserAccount() {
        return repository.getAllUserAccount();
    }

    @PostMapping("/deleteMultiple")
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

    @PostMapping("/updatePassword")
    ResponseEntity<ResponseObject> updatePassword(@RequestParam String userId, @RequestParam String newPassword) {
        if(repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).size() > 0) {
                repository.changePassword(SecurityContextHolder.getContext().getAuthentication().getName(), passwordEncoder.encode(newPassword));
                return ResponseEntity.status((HttpStatus.OK)).body(
                        new ResponseObject("ok", "Change password successfully", "")
                );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "Cannot find user id", "")
        );
    }
}
