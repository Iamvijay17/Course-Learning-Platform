package com.course_learning.backend.repository;

import com.course_learning.backend.model.Enrollment;
import com.course_learning.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUser_UserId(Long userId);

    List<Enrollment> findByCourse_CourseId(Long courseId);

    List<Enrollment> findByUser_UserIdAndStatus(Long userId, Enrollment.EnrollmentStatus status);

    List<Enrollment> findByCourse_CourseIdAndStatus(Long courseId, Enrollment.EnrollmentStatus status);

    List<Enrollment> findByStatus(Enrollment.EnrollmentStatus status);

    Optional<Enrollment> findByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);

    boolean existsByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.courseId = :courseId")
    long countByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.user.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.status = :status")
    long countByStatus(@Param("status") Enrollment.EnrollmentStatus status);
}
