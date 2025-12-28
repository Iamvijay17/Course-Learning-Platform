package com.course_learning.backend.service;

import com.course_learning.backend.dto.UserDto;
import com.course_learning.backend.model.User;
import com.course_learning.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    public UserDto createUser(UserDto userDto, String rawPassword) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }

        User user = convertToEntity(userDto);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(User.UserRole.STUDENT); // Default role
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // Check if username is being changed and if it conflicts
                    if (!existingUser.getUsername().equals(userDto.getUsername()) &&
                        userRepository.existsByUsername(userDto.getUsername())) {
                        throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
                    }
                    // Check if email is being changed and if it conflicts
                    if (!existingUser.getEmail().equals(userDto.getEmail()) &&
                        userRepository.existsByEmail(userDto.getEmail())) {
                        throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
                    }

                    updateUserFromDto(existingUser, userDto);
                    User updatedUser = userRepository.save(existingUser);
                    return convertToDto(updatedUser);
                });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserDto> getUsersByRole(String role) {
        try {
            User.UserRole userRole = User.UserRole.valueOf(role.toUpperCase());
            return userRepository.findByRole(userRole).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public List<UserDto> searchUsers(String keyword) {
        return userRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public long countUsersByRole(String role) {
        try {
            User.UserRole userRole = User.UserRole.valueOf(role.toUpperCase());
            return userRepository.countByRole(userRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().toString(),
                user.getIsActive()
        );
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        if (userDto.getRole() != null) {
            user.setRole(User.UserRole.valueOf(userDto.getRole()));
        }
        user.setIsActive(userDto.getIsActive() != null ? userDto.getIsActive() : true);
        return user;
    }

    private void updateUserFromDto(User user, UserDto userDto) {
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        if (userDto.getRole() != null) {
            user.setRole(User.UserRole.valueOf(userDto.getRole()));
        }
        if (userDto.getIsActive() != null) {
            user.setIsActive(userDto.getIsActive());
        }
    }
}
