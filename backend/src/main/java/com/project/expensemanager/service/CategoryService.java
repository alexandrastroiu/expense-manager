package com.project.expensemanager.service;

import com.project.expensemanager.dto.category.CategoryResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    // Inject repository
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Business logic
    // Read
    public List<CategoryResponse> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        return allCategories.stream().map(this::mapToResponse).toList();
    }

    public CategoryResponse getCategoryById(Integer categoryId) {
        Category category =  categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));

        return mapToResponse(category);
    }

    // Helper methods
    // Map entity to response
    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getCategoryName());
    }

    // Map request to entity
    private Category mapToEntity(CategoryResponse request) {
        return new Category(request.categoryName());
    }

    private Category getCategoryEntityById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
