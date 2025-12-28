package com.course_learning.backend.controller;

import com.course_learning.backend.dto.InstructorDto;
import com.course_learning.backend.service.InstructorService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/instructors")
@Tag(name = "Instructor Management", description = "APIs for managing instructors in the learning platform")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping
    @Operation(summary = "Get all instructors", description = "Retrieve a list of all instructors in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved instructors")
    })
    public ResponseEntity<List<InstructorDto>> getAllInstructors() {
        List<InstructorDto> instructors = instructorService.getAllInstructors();
        return ResponseEntity.ok(instructors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get instructor by ID", description = "Retrieve a specific instructor by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor found"),
        @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    public ResponseEntity<InstructorDto> getInstructorById(
        @Parameter(description = "Instructor ID") @PathVariable Long id) {
        Optional<InstructorDto> instructor = instructorService.getInstructorById(id);
        return instructor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get instructor by email", description = "Retrieve a specific instructor by their email address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor found"),
        @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    public ResponseEntity<InstructorDto> getInstructorByEmail(
        @Parameter(description = "Instructor email") @PathVariable String email) {
        Optional<InstructorDto> instructor = instructorService.getInstructorByEmail(email);
        return instructor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new instructor", description = "Create a new instructor with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Instructor created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or email already exists")
    })
    public ResponseEntity<InstructorDto> createInstructor(@Valid @RequestBody InstructorDto instructorDto) {
        try {
            InstructorDto createdInstructor = instructorService.createInstructor(instructorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInstructor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update instructor", description = "Update an existing instructor with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor updated successfully"),
        @ApiResponse(responseCode = "404", description = "Instructor not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or email already exists")
    })
    public ResponseEntity<InstructorDto> updateInstructor(
        @Parameter(description = "Instructor ID") @PathVariable Long id,
        @Valid @RequestBody InstructorDto instructorDto) {
        try {
            Optional<InstructorDto> updatedInstructor = instructorService.updateInstructor(id, instructorDto);
            return updatedInstructor.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete instructor", description = "Delete an instructor by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Instructor deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Instructor not found")
    })
    public ResponseEntity<Void> deleteInstructor(
        @Parameter(description = "Instructor ID") @PathVariable Long id) {
        boolean deleted = instructorService.deleteInstructor(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Get instructors by specialization", description = "Retrieve instructors filtered by their specialization")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved instructors")
    })
    public ResponseEntity<List<InstructorDto>> getInstructorsBySpecialization(
        @Parameter(description = "Instructor specialization") @PathVariable String specialization) {
        List<InstructorDto> instructors = instructorService.getInstructorsBySpecialization(specialization);
        return ResponseEntity.ok(instructors);
    }

    @GetMapping("/search")
    @Operation(summary = "Search instructors", description = "Search instructors by keyword in name or specialization")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved instructors")
    })
    public ResponseEntity<List<InstructorDto>> searchInstructors(
        @Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<InstructorDto> instructors = instructorService.searchInstructors(keyword);
        return ResponseEntity.ok(instructors);
    }

    @GetMapping("/experience/{minYears}")
    @Operation(summary = "Get instructors by experience", description = "Retrieve instructors with minimum years of experience")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved instructors")
    })
    public ResponseEntity<List<InstructorDto>> getInstructorsByExperience(
        @Parameter(description = "Minimum experience years") @PathVariable Integer minYears) {
        List<InstructorDto> instructors = instructorService.getInstructorsByExperience(minYears);
        return ResponseEntity.ok(instructors);
    }

    @GetMapping("/count/specialization/{specialization}")
    @Operation(summary = "Count instructors by specialization", description = "Get the count of instructors with a specific specialization")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> countInstructorsBySpecialization(
        @Parameter(description = "Instructor specialization") @PathVariable String specialization) {
        long count = instructorService.countInstructorsBySpecialization(specialization);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count")
    @Operation(summary = "Get total instructor count", description = "Get the total number of instructors in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> getTotalInstructorCount() {
        long count = instructorService.getTotalInstructorCount();
        return ResponseEntity.ok(count);
    }
}
