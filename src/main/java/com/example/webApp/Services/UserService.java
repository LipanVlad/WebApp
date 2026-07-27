package com.example.webApp.Services;

import com.example.webApp.Config.JwtUtil;
import com.example.webApp.DataTransferObjects.PostRequestDTO;
import com.example.webApp.DataTransferObjects.PostResponseDTO;
import com.example.webApp.DataTransferObjects.UserRequestDTO;
import com.example.webApp.DataTransferObjects.UserResponseDTO;
import com.example.webApp.Entities.Post;
import com.example.webApp.Entities.User;
import com.example.webApp.Exceptions.DoesNotExistException;
import com.example.webApp.Exceptions.NameAlreadyExistsException;
import com.example.webApp.Exceptions.InvalidInputException;
import com.example.webApp.Repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    private UserResponseDTO userToDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setCreationTime(user.getCreationTime());
        userResponseDTO.setToken(jwtUtil.generateToken(user.getUsername()));
        return userResponseDTO;
    }

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO){
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

    public UserResponseDTO loginUser(UserRequestDTO userDto){
        User theUser = userRepo.findByUsername(userDto.getUsername())
                .orElseThrow( () -> new DoesNotExistException("Invalid credentials"));
        if(!passwordEncoder.matches(userDto.getPassword(), theUser.getPassword())){
            throw new InvalidInputException("Invalid credentials");
        }
        return userToDTO(theUser);
    }

    public void deleteUser(String userName){
       User user = userRepo.findByUsername(userName)
                .orElseThrow(() -> new DoesNotExistException("User not found"));
       user.setPassword("<DELETED>");
        user.setUsername("<DELETED>_" + user.getId());
        user.setDeleted(true);
       userRepo.save(user);
    }

    public UserResponseDTO patchUser(UserRequestDTO userRequestDTO, Long userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new DoesNotExistException("User does not exist"));

        if(userRequestDTO.getUsername() != null && !userRequestDTO.getUsername().equalsIgnoreCase(user.getUsername())){
            user.setUsername(userRequestDTO.getUsername());
        }
        if(userRequestDTO.getPassword() != null && !passwordEncoder.matches(userRequestDTO.getPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        userRepo.save(user);
        UserResponseDTO userResponseDTO = userToDTO(user);
        return userResponseDTO;
    }

    public UserResponseDTO getUser(Long userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new DoesNotExistException("User does not exist"));
        UserResponseDTO userResponseDTO = userToDTO(user);
        return userResponseDTO;
    }
    public List<UserResponseDTO> getUsers(){
        List<User> users = userRepo.findAll();
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        for (User u : users) {
            userResponseDTOList.add(userToDTO(u));
        }
        return userResponseDTOList;
    }

}
