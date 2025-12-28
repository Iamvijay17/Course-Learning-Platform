package com.course_learning.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {

    private Long categoryId;

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;

    // Constructors
    public CategoryDto() {}

    public CategoryDto(Long categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
