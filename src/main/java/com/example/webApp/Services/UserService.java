package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.UserRequestDTO;
import com.example.webApp.DataTransferObjects.UserResponseDTO;
import com.example.webApp.Entities.User;
import com.example.webApp.Exceptions.NameAlreadyExistsException;
import com.example.webApp.Exceptions.InvalidInputException;
import com.example.webApp.Repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponseDTO userToDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setCreationTime(user.getCreationTime());

        return userResponseDTO;
    }

    private void credentialCheck(UserRequestDTO userRequestDTO){
        if (userRequestDTO.getUsername() == null || userRequestDTO.getUsername().isEmpty() ||
                userRequestDTO.getPassword() == null || userRequestDTO.getPassword().isEmpty()) {
            throw new InvalidInputException("Invalid credentials");
        }
    }
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO){
        credentialCheck(userRequestDTO);

        Optional<User> optionalUser = userRepo.findByUsername(userRequestDTO.getUsername());
        if(optionalUser.isPresent()){
            throw new NameAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setCreationTime(LocalDateTime.now());
        userRepo.save(user);
        UserResponseDTO userResponseDTO = userToDTO(user);
        return userResponseDTO;
    }

    public void loginUser(UserRequestDTO userDto){
        credentialCheck(userDto);

        Optional<User> optionalUser = userRepo.findByUsername(userDto.getUsername());
        if(optionalUser.isEmpty()){
            throw new InvalidInputException("Invalid credentials");
        }

        User theUser = optionalUser.get();
        if(!passwordEncoder.matches(userDto.getPassword(), theUser.getPassword())){
            throw new InvalidInputException("Invalid credentials");
        }
    }
}
