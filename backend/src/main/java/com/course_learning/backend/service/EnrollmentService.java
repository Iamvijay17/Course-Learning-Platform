package com.course_learning.backend.service;

import com.course_learning.backend.dto.EnrollmentDto;
import com.course_learning.backend.model.Enrollment;
import com.course_learning.backend.model.User;
import com.course_learning.backend.model.Course;
import com.course_learning.backend.repository.EnrollmentRepository;
import com.course_learning.backend.repository.UserRepository;
import com.course_learning.backend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<EnrollmentDto> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<EnrollmentDto> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<EnrollmentDto> getEnrollmentsByUser(Long userId) {
        return enrollmentRepository.findByUser_UserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDto> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourse_CourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EnrollmentDto enrollUserInCourse(Long userId, Long courseId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Check if course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        // Check if already enrolled
        if (enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId)) {
            throw new IllegalArgumentException("User is already enrolled in this course");
        }

        // Check if course is published
        if (course.getStatus() != Course.CourseStatus.PUBLISHED) {
            throw new IllegalArgumentException("Cannot enroll in a course that is not published");
        }

        Enrollment enrollment = new Enrollment(user, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDto(savedEnrollment);
    }

    public Optional<EnrollmentDto> updateEnrollmentProgress(Long enrollmentId, Integer progressPercentage) {
        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> {
                    enrollment.setProgressPercentage(progressPercentage);

                    // Auto-complete if progress reaches 100%
                    if (progressPercentage >= 100) {
                        enrollment.setStatus(Enrollment.EnrollmentStatus.COMPLETED);
                        enrollment.setCompletedAt(LocalDateTime.now());
                    }

                    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
                    return convertToDto(updatedEnrollment);
                });
    }

    public Optional<EnrollmentDto> completeEnrollment(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> {
                    enrollment.setStatus(Enrollment.EnrollmentStatus.COMPLETED);
                    enrollment.setProgressPercentage(100);
                    enrollment.setCompletedAt(LocalDateTime.now());
                    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
                    return convertToDto(updatedEnrollment);
                });
    }

    public Optional<EnrollmentDto> dropEnrollment(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> {
                    enrollment.setStatus(Enrollment.EnrollmentStatus.DROPPED);
                    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
                    return convertToDto(updatedEnrollment);
                });
    }

    public boolean unenrollUserFromCourse(Long userId, Long courseId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
        if (enrollment.isPresent()) {
            enrollmentRepository.delete(enrollment.get());
            return true;
        }
        return false;
    }

    public List<EnrollmentDto> getEnrollmentsByStatus(String status) {
        try {
            Enrollment.EnrollmentStatus enrollmentStatus = Enrollment.EnrollmentStatus.valueOf(status.toUpperCase());
            return enrollmentRepository.findByStatus(enrollmentStatus).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    public long countEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }

    public long countEnrollmentsByUser(Long userId) {
        return enrollmentRepository.countByUserId(userId);
    }

    public long countEnrollmentsByStatus(String status) {
        try {
            Enrollment.EnrollmentStatus enrollmentStatus = Enrollment.EnrollmentStatus.valueOf(status.toUpperCase());
            return enrollmentRepository.countByStatus(enrollmentStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    public long getTotalEnrollmentCount() {
        return enrollmentRepository.count();
    }

    private EnrollmentDto convertToDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto(
                enrollment.getEnrollmentId(),
                enrollment.getUser().getUserId(),
                enrollment.getCourse().getCourseId(),
                enrollment.getStatus().toString(),
                enrollment.getEnrolledAt(),
                enrollment.getCompletedAt(),
                enrollment.getProgressPercentage()
        );
        dto.setUserName(enrollment.getUser().getFullName());
        dto.setCourseTitle(enrollment.getCourse().getTitle());
        return dto;
    }
}
