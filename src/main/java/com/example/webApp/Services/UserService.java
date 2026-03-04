package com.example.webApp.Services;

import com.example.webApp.DataTransferObjects.UserDTO;
import com.example.webApp.Entities.User;
import com.example.webApp.Exceptions.NameAlreadyExistsException;
import com.example.webApp.Exceptions.InvalidInputException;
import com.example.webApp.Repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private void credentialCheck(UserDTO userDto){
        if (userDto.getUsername() == null || userDto.getUsername().isEmpty() ||
                userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new InvalidInputException("Invalid credentials");
        }
    }
    public void checkAndRegisterUser(UserDTO userDto){
        credentialCheck(userDto);

        Optional<User> optionalUser = userRepo.findByUsername(userDto.getUsername());
        if(optionalUser.isPresent()){
            throw new NameAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public void checkAndLoginUser(UserDTO userDto){
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
