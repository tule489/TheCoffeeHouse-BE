package com.theCoffeeHouse.theCoffeeHouse.Controller;

import com.theCoffeeHouse.theCoffeeHouse.Models.AuthenticationRequest;
import com.theCoffeeHouse.theCoffeeHouse.Models.ChangePasswordRequest;
import com.theCoffeeHouse.theCoffeeHouse.Models.RegisterRequest;
import com.theCoffeeHouse.theCoffeeHouse.Models.ResponseObject;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.UserRepository;
import com.theCoffeeHouse.theCoffeeHouse.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/auth")
@CrossOrigin()
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @Autowired
    private UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody RegisterRequest request) {
        if (service.register(request).getToken() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("failed","Username already exist", ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Register successfully", ""));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<ResponseObject> registerAdminAccount(@RequestBody RegisterRequest request) {
        if (service.registerAdminAccount(request).getToken() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("failed","Username already exist", ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Register successfully", ""));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Login successfully", service.authenticate(request)));
    }

    @PostMapping("/changePassword")
    ResponseEntity<ResponseObject> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if(repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).size() > 0) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(SecurityContextHolder.getContext().getAuthentication().getName(), changePasswordRequest.getOldPassword()));
                repository.changePassword(SecurityContextHolder.getContext().getAuthentication().getName(), passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                return ResponseEntity.status((HttpStatus.OK)).body(
                        new ResponseObject("ok", "Change password successfully", "")
                );
            } catch (AuthenticationException e) {
                return ResponseEntity.status((HttpStatus.OK)).body(
                        new ResponseObject("failed", "Sai mật khẩu", "")
                );
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                new ResponseObject("failed", "Cannot find user id", "")
        );
    }
}
