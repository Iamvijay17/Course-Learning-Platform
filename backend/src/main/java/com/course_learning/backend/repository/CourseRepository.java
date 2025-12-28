package com.course_learning.backend.repository;

import com.course_learning.backend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByCategoryId(Long categoryId);

    List<Course> findByStatus(Course.CourseStatus status);

    @Query("SELECT c FROM Course c WHERE c.title LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<Course> searchByKeyword(@Param("keyword") String keyword);

    List<Course> findByInstructorIdAndStatus(Long instructorId, Course.CourseStatus status);

    long countByStatus(Course.CourseStatus status);
}
