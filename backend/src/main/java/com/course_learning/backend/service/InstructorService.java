package com.course_learning.backend.service;

import com.course_learning.backend.dto.InstructorDto;
import com.course_learning.backend.model.Instructor;
import com.course_learning.backend.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    public List<InstructorDto> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<InstructorDto> getInstructorById(Long id) {
        return instructorRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<InstructorDto> getInstructorByEmail(String email) {
        return instructorRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    public InstructorDto createInstructor(InstructorDto instructorDto) {
        if (instructorRepository.existsByEmail(instructorDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + instructorDto.getEmail());
        }
        Instructor instructor = convertToEntity(instructorDto);
        Instructor savedInstructor = instructorRepository.save(instructor);
        return convertToDto(savedInstructor);
    }

    public Optional<InstructorDto> updateInstructor(Long id, InstructorDto instructorDto) {
        return instructorRepository.findById(id)
                .map(existingInstructor -> {
                    // Check if email is being changed and if it conflicts
                    if (!existingInstructor.getEmail().equals(instructorDto.getEmail()) &&
                        instructorRepository.existsByEmail(instructorDto.getEmail())) {
                        throw new IllegalArgumentException("Email already exists: " + instructorDto.getEmail());
                    }
                    updateInstructorFromDto(existingInstructor, instructorDto);
                    Instructor updatedInstructor = instructorRepository.save(existingInstructor);
                    return convertToDto(updatedInstructor);
                });
    }

    public boolean deleteInstructor(Long id) {
        if (instructorRepository.existsById(id)) {
            instructorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<InstructorDto> getInstructorsBySpecialization(String specialization) {
        return instructorRepository.findBySpecialization(specialization).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InstructorDto> searchInstructors(String keyword) {
        return instructorRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InstructorDto> getInstructorsByExperience(Integer minYears) {
        return instructorRepository.findByExperienceYearsGreaterThanEqual(minYears).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public long countInstructorsBySpecialization(String specialization) {
        return instructorRepository.countBySpecialization(specialization);
    }

    public long getTotalInstructorCount() {
        return instructorRepository.count();
    }

    private InstructorDto convertToDto(Instructor instructor) {
        return new InstructorDto(
                instructor.getInstructorId(),
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getEmail(),
                instructor.getBio(),
                instructor.getSpecialization(),
                instructor.getExperienceYears()
        );
    }

    private Instructor convertToEntity(InstructorDto instructorDto) {
        Instructor instructor = new Instructor();
        instructor.setFirstName(instructorDto.getFirstName());
        instructor.setLastName(instructorDto.getLastName());
        instructor.setEmail(instructorDto.getEmail());
        instructor.setBio(instructorDto.getBio());
        instructor.setSpecialization(instructorDto.getSpecialization());
        instructor.setExperienceYears(instructorDto.getExperienceYears());
        return instructor;
    }

    private void updateInstructorFromDto(Instructor instructor, InstructorDto instructorDto) {
        instructor.setFirstName(instructorDto.getFirstName());
        instructor.setLastName(instructorDto.getLastName());
        instructor.setEmail(instructorDto.getEmail());
        instructor.setBio(instructorDto.getBio());
        instructor.setSpecialization(instructorDto.getSpecialization());
        instructor.setExperienceYears(instructorDto.getExperienceYears());
    }
}
