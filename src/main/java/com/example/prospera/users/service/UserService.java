package com.example.prospera.users.service;

import com.example.prospera.users.UserEntity;
import com.example.prospera.users.UserRepository;
import com.example.prospera.users.bo.RegisterRequest;
import com.example.prospera.users.bo.UserResponse;
import com.example.prospera.property.PropertyRepository;
import com.example.prospera.property.entity.PropertyEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public UserService(UserRepository userRepository, PropertyRepository propertyRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }

    public UserResponse updateUser(RegisterRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        UserEntity userEntity = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // if (userOptional.isPresent()) {
        // UserEntity userEntity = userOptional.get();

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

        // Save the updated user
        userEntity = userRepository.save(userEntity);

        // Create and return UserResponse
        UserResponse response = new UserResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getBalance());

        return response;
        // } else {
        // // If user not found, you can handle it in various ways
        // throw new IllegalArgumentException("User with ID " + id + " not found.");
        // }
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

    public String likeProperty(Long propertyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        UserEntity user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property with ID " + propertyId + " not found."));

        if (user.getLikedProperties().contains(propertyId)) {
            return "Property with ID " + propertyId + " is already in liked properties.";
        }

        user.addToLikedProperties(propertyId);
        userRepository.save(user);

        return "Property with ID " + propertyId + " has been added to liked properties.";
    }

    public String unlikeProperty(Long propertyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        UserEntity user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property with ID " + propertyId + " not found."));

        user.removeFromLikedProperties(propertyId);
        userRepository.save(user);

        return "Property with ID " + propertyId + " has been removed from liked properties.";
    }

    public List<PropertyEntity> getLikedProperties() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        UserEntity user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        List<Long> likedProperties = user.getLikedProperties();
        List<PropertyEntity> properties = new ArrayList<>();

        for (Long propertyId : likedProperties) {
            PropertyEntity property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new IllegalArgumentException("Property with ID " + propertyId + " not found."));
            properties.add(property);
        }

        return properties;
    }
}
