package com.example.prospera.users.service;
import com.example.prospera.users.UserEntity;
import com.example.prospera.users.UserRepository;
import com.example.prospera.users.bo.RegisterRequest;
import com.example.prospera.users.bo.UserResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> allUsers() {
        List<UserEntity> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public UserResponse updateUser(Long id, RegisterRequest request) {
        // Find the user by id
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();

            // Update fields
            if (request.getFirstName() != null) {
                userEntity.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                userEntity.setLastName(request.getLastName());
            }
            if (request.getEmail() != null) {
                userEntity.setEmail(request.getEmail());
            }
            if (request.getPassword() != null) {
                userEntity.setPassword(request.getPassword());
            }

            if (request.getBalance() != null) {
                userEntity.setBalance(request.getBalance());
            }

            // Save the updated user
            userEntity = userRepository.save(userEntity);

            // Create and return UserResponse
            UserResponse response = new UserResponse(
                    userEntity.getId(),
                    userEntity.getEmail(),
                    userEntity.getFirstName(),
                    userEntity.getLastName(),
                    userEntity.getBalance()

            );

            return response;
        } else {
            // If user not found, you can handle it in various ways
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
    }

    public UserResponse getUserById(Long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            // Convert UserEntity to UserResponse
            return new UserResponse(
                    userEntity.getId(),
                    userEntity.getEmail(),
                    userEntity.getFirstName(),
                    userEntity.getLastName(),
                    userEntity.getBalance()

            );
        } else {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
    }

    public void deleteUserById(Long id) {
        // Check if the user exists
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            // User found, delete the user
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
    }
}
