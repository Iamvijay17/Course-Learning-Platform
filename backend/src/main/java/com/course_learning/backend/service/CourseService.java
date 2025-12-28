package com.course_learning.backend.service;

import com.course_learning.backend.dto.CourseDto;
import com.course_learning.backend.model.Course;
import com.course_learning.backend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<CourseDto> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(this::convertToDto);
    }

    public CourseDto createCourse(CourseDto courseDto) {
        Course course = convertToEntity(courseDto);
        course.setStatus(Course.CourseStatus.DRAFT);
        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    public Optional<CourseDto> updateCourse(Long id, CourseDto courseDto) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    updateCourseFromDto(existingCourse, courseDto);
                    Course updatedCourse = courseRepository.save(existingCourse);
                    return convertToDto(updatedCourse);
                });
    }

    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CourseDto> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<CourseDto> publishCourse(Long id) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setStatus(Course.CourseStatus.PUBLISHED);
                    Course updatedCourse = courseRepository.save(course);
                    return convertToDto(updatedCourse);
                });
    }

    private CourseDto convertToDto(Course course) {
        return new CourseDto(
                course.getCourseId(),
                course.getTitle(),
                course.getDescription(),
                course.getInstructorId(),
                course.getCategoryId(),
                course.getPrice(),
                course.getDuration(),
                course.getStatus().toString()
        );
    }

    private Course convertToEntity(CourseDto courseDto) {
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setInstructorId(courseDto.getInstructorId());
        course.setCategoryId(courseDto.getCategoryId());
        course.setPrice(courseDto.getPrice());
        course.setDuration(courseDto.getDuration());
        if (courseDto.getStatus() != null) {
            course.setStatus(Course.CourseStatus.valueOf(courseDto.getStatus()));
        }
        return course;
    }

    private void updateCourseFromDto(Course course, CourseDto courseDto) {
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setInstructorId(courseDto.getInstructorId());
        course.setCategoryId(courseDto.getCategoryId());
        course.setPrice(courseDto.getPrice());
        course.setDuration(courseDto.getDuration());
        if (courseDto.getStatus() != null) {
            course.setStatus(Course.CourseStatus.valueOf(courseDto.getStatus()));
        }
    }
}
