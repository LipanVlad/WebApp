package com.example.webApp.Controllers;

import com.example.webApp.DataTransferObjects.PostRequestDTO;
import com.example.webApp.DataTransferObjects.PostResponseDTO;
import com.example.webApp.DataTransferObjects.UserRequestDTO;
import com.example.webApp.DataTransferObjects.UserResponseDTO;
import com.example.webApp.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = service.registerUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserRequestDTO userDto){
        UserResponseDTO userResponseDTO = service.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponseDTO);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        service.deleteUser(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<?> patchUser(@RequestBody UserRequestDTO userRequestDTO, @PathVariable Long userId){
        UserResponseDTO userResponseDTO = service.patchUser(userRequestDTO, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponseDTO);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId){
        UserResponseDTO userResponseDTO = service.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        List<UserResponseDTO> userResponseDTOList = service.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTOList);
    }
}
