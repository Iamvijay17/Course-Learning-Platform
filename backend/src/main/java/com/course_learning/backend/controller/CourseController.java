package com.course_learning.backend.controller;

import com.course_learning.backend.dto.CourseDto;
import com.course_learning.backend.service.CourseService;
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
@RequestMapping("/api/courses")
@Tag(name = "Course Management", description = "APIs for managing courses in the learning platform")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieve a list of all courses in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Retrieve a specific course by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course found"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseDto> getCourseById(
        @Parameter(description = "Course ID") @PathVariable Long id) {
        Optional<CourseDto> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new course", description = "Create a new course with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto) {
        CourseDto createdCourse = courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course", description = "Update an existing course with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course updated successfully"),
        @ApiResponse(responseCode = "404", description = "Course not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CourseDto> updateCourse(
        @Parameter(description = "Course ID") @PathVariable Long id,
        @Valid @RequestBody CourseDto courseDto) {
        Optional<CourseDto> updatedCourse = courseService.updateCourse(id, courseDto);
        return updatedCourse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Delete a course by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<Void> deleteCourse(
        @Parameter(description = "Course ID") @PathVariable Long id) {
        boolean deleted = courseService.deleteCourse(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get courses by instructor", description = "Retrieve all courses taught by a specific instructor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    public ResponseEntity<List<CourseDto>> getCoursesByInstructor(
        @Parameter(description = "Instructor ID") @PathVariable Long instructorId) {
        List<CourseDto> courses = courseService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get courses by category", description = "Retrieve all courses in a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    public ResponseEntity<List<CourseDto>> getCoursesByCategory(
        @Parameter(description = "Category ID") @PathVariable Long categoryId) {
        List<CourseDto> courses = courseService.getCoursesByCategory(categoryId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/search")
    @Operation(summary = "Search courses", description = "Search courses by keyword in title or description")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    public ResponseEntity<List<CourseDto>> searchCourses(
        @Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<CourseDto> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish course", description = "Change course status to PUBLISHED")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course published successfully"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseDto> publishCourse(
        @Parameter(description = "Course ID") @PathVariable Long id) {
        Optional<CourseDto> publishedCourse = courseService.publishCourse(id);
        return publishedCourse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get courses by status", description = "Retrieve courses filtered by their status (DRAFT, PUBLISHED, ARCHIVED)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    public ResponseEntity<List<CourseDto>> getCoursesByStatus(
        @Parameter(description = "Course status") @PathVariable String status) {
        try {
            List<CourseDto> courses = courseService.getCoursesByStatus(status);
            return ResponseEntity.ok(courses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/instructor/{instructorId}/status/{status}")
    @Operation(summary = "Get instructor courses by status", description = "Retrieve courses by instructor and status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    public ResponseEntity<List<CourseDto>> getCoursesByInstructorAndStatus(
        @Parameter(description = "Instructor ID") @PathVariable Long instructorId,
        @Parameter(description = "Course status") @PathVariable String status) {
        try {
            List<CourseDto> courses = courseService.getCoursesByInstructorAndStatus(instructorId, status);
            return ResponseEntity.ok(courses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Count courses by status", description = "Get the count of courses with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
    })
    public ResponseEntity<Long> countCoursesByStatus(
        @Parameter(description = "Course status") @PathVariable String status) {
        try {
            long count = courseService.countCoursesByStatus(status);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive course", description = "Change course status to ARCHIVED")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course archived successfully"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseDto> archiveCourse(
        @Parameter(description = "Course ID") @PathVariable Long id) {
        Optional<CourseDto> archivedCourse = courseService.archiveCourse(id);
        return archivedCourse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/draft")
    @Operation(summary = "Move course to draft", description = "Change course status to DRAFT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course moved to draft successfully"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseDto> draftCourse(
        @Parameter(description = "Course ID") @PathVariable Long id) {
        Optional<CourseDto> draftCourse = courseService.draftCourse(id);
        return draftCourse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
