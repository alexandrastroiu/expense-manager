package com.project.expensemanager.service;

import com.project.expensemanager.dto.category.CategoryResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    // Inject repository
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Read
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Map entity to response
    private CategoryResponse mapToResponse(Category category) {
            return new CategoryResponse(category.getId(), category.getCategoryName());
    }

    // Map request to entity
    private Category mapToEntity(CategoryResponse request) {
            return new Category(request.categoryName());
    }
}
