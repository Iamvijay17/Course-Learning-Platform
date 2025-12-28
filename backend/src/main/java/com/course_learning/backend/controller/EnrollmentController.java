package com.course_learning.backend.controller;

import com.course_learning.backend.dto.EnrollmentDto;
import com.course_learning.backend.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollment Management", description = "APIs for managing course enrollments in the learning platform")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Retrieve a list of all course enrollments in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved enrollments")
    })
    public ResponseEntity<List<EnrollmentDto>> getAllEnrollments() {
        List<EnrollmentDto> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Retrieve a specific enrollment by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Enrollment found"),
        @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<EnrollmentDto> getEnrollmentById(
        @Parameter(description = "Enrollment ID") @PathVariable Long id) {
        Optional<EnrollmentDto> enrollment = enrollmentService.getEnrollmentById(id);
        return enrollment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get enrollments by user", description = "Retrieve all course enrollments for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved enrollments")
    })
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByUser(
        @Parameter(description = "User ID") @PathVariable Long userId) {
        List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByUser(userId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get enrollments by course", description = "Retrieve all enrollments for a specific course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved enrollments")
    })
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByCourse(
        @Parameter(description = "Course ID") @PathVariable Long courseId) {
        List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping
    @Operation(summary = "Enroll user in course", description = "Enroll a user in a specific course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Enrollment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or enrollment already exists")
    })
    public ResponseEntity<EnrollmentDto> enrollUserInCourse(
        @Parameter(description = "User ID") @RequestParam Long userId,
        @Parameter(description = "Course ID") @RequestParam Long courseId) {
        try {
            EnrollmentDto enrollment = enrollmentService.enrollUserInCourse(userId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/progress")
    @Operation(summary = "Update enrollment progress", description = "Update the progress percentage for an enrollment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Progress updated successfully"),
        @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<EnrollmentDto> updateEnrollmentProgress(
        @Parameter(description = "Enrollment ID") @PathVariable Long id,
        @Parameter(description = "Progress percentage") @RequestParam Integer progressPercentage) {
        Optional<EnrollmentDto> updatedEnrollment = enrollmentService.updateEnrollmentProgress(id, progressPercentage);
        return updatedEnrollment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete enrollment", description = "Mark an enrollment as completed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Enrollment completed successfully"),
        @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<EnrollmentDto> completeEnrollment(
        @Parameter(description = "Enrollment ID") @PathVariable Long id) {
        Optional<EnrollmentDto> completedEnrollment = enrollmentService.completeEnrollment(id);
        return completedEnrollment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/drop")
    @Operation(summary = "Drop enrollment", description = "Mark an enrollment as dropped")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Enrollment dropped successfully"),
        @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<EnrollmentDto> dropEnrollment(
        @Parameter(description = "Enrollment ID") @PathVariable Long id) {
        Optional<EnrollmentDto> droppedEnrollment = enrollmentService.dropEnrollment(id);
        return droppedEnrollment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/user/{userId}/course/{courseId}")
    @Operation(summary = "Unenroll user from course", description = "Remove a user's enrollment from a specific course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Unenrollment successful"),
        @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<Void> unenrollUserFromCourse(
        @Parameter(description = "User ID") @PathVariable Long userId,
        @Parameter(description = "Course ID") @PathVariable Long courseId) {
        boolean unenrolled = enrollmentService.unenrollUserFromCourse(userId, courseId);
        return unenrolled ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get enrollments by status", description = "Retrieve enrollments filtered by their status (ACTIVE, COMPLETED, DROPPED)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved enrollments")
    })
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStatus(
        @Parameter(description = "Enrollment status") @PathVariable String status) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStatus(status);
            return ResponseEntity.ok(enrollments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count/course/{courseId}")
    @Operation(summary = "Count enrollments by course", description = "Get the number of enrollments for a specific course")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> countEnrollmentsByCourse(
        @Parameter(description = "Course ID") @PathVariable Long courseId) {
        long count = enrollmentService.countEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/user/{userId}")
    @Operation(summary = "Count enrollments by user", description = "Get the number of courses a user is enrolled in")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> countEnrollmentsByUser(
        @Parameter(description = "User ID") @PathVariable Long userId) {
        long count = enrollmentService.countEnrollmentsByUser(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Count enrollments by status", description = "Get the count of enrollments with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> countEnrollmentsByStatus(
        @Parameter(description = "Enrollment status") @PathVariable String status) {
        try {
            long count = enrollmentService.countEnrollmentsByStatus(status);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get total enrollment count", description = "Get the total number of enrollments in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> getTotalEnrollmentCount() {
        long count = enrollmentService.getTotalEnrollmentCount();
        return ResponseEntity.ok(count);
    }
}
