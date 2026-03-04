package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.UserDTO;
import com.example.webApp.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto){
        service.checkAndRegisterUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDto){
        service.checkAndLoginUser(userDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Logged in");
    }
}
