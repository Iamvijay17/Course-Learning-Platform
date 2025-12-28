package com.course_learning.backend.repository;

import com.course_learning.backend.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Optional<Instructor> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Instructor> findBySpecialization(String specialization);

    @Query("SELECT i FROM Instructor i WHERE i.firstName LIKE %:keyword% OR i.lastName LIKE %:keyword% OR i.specialization LIKE %:keyword%")
    List<Instructor> searchByKeyword(@Param("keyword") String keyword);

    List<Instructor> findByExperienceYearsGreaterThanEqual(Integer years);

    long countBySpecialization(String specialization);
}
