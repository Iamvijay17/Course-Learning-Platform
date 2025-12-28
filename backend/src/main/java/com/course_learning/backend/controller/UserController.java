package com.course_learning.backend.controller;

import com.course_learning.backend.dto.UserDto;
import com.course_learning.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users in the learning platform")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> getUserById(
        @Parameter(description = "User ID") @PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user account with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or username/email already exists")
    })
    public ResponseEntity<UserDto> createUser(
        @Valid @RequestBody UserDto userDto,
        @RequestParam String password) {
        try {
            UserDto createdUser = userService.createUser(userDto, password);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or username/email already exists")
    })
    public ResponseEntity<UserDto> updateUser(
        @Parameter(description = "User ID") @PathVariable Long id,
        @Valid @RequestBody UserDto userDto) {
        try {
            Optional<UserDto> updatedUser = userService.updateUser(id, userDto);
            return updatedUser.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "User ID") @PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role", description = "Retrieve users filtered by their role (STUDENT, INSTRUCTOR, ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    })
    public ResponseEntity<List<UserDto>> getUsersByRole(
        @Parameter(description = "User role") @PathVariable String role) {
        try {
            List<UserDto> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search users by keyword in name or username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    })
    public ResponseEntity<List<UserDto>> searchUsers(
        @Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<UserDto> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count/role/{role}")
    @Operation(summary = "Count users by role", description = "Get the count of users with a specific role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> countUsersByRole(
        @Parameter(description = "User role") @PathVariable String role) {
        try {
            long count = userService.countUsersByRole(role);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get total user count", description = "Get the total number of users in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> getTotalUserCount() {
        long count = userService.getTotalUserCount();
        return ResponseEntity.ok(count);
    }
}
