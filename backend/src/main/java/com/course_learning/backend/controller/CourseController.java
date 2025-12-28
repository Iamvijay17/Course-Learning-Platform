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
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) {
        Optional<CourseDto> updatedCourse = courseService.updateCourse(id, courseDto);
        return updatedCourse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        boolean deleted = courseService.deleteCourse(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDto>> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<CourseDto> courses = courseService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseDto>> getCoursesByCategory(@PathVariable Long categoryId) {
        List<CourseDto> courses = courseService.getCoursesByCategory(categoryId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCourses(@RequestParam String keyword) {
        List<CourseDto> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<CourseDto> publishCourse(@PathVariable Long id) {
        Optional<CourseDto> publishedCourse = courseService.publishCourse(id);
        return publishedCourse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
