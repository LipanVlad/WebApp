package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.UserRequestDTO;
import com.example.webApp.DataTransferObjects.UserResponseDTO;
import com.example.webApp.Services.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = service.registerUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserRequestDTO userDto){
        service.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Logged in");
    }
}
