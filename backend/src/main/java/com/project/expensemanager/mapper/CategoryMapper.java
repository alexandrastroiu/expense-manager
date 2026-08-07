package com.project.expensemanager.mapper;

import com.project.expensemanager.dto.category.CategoryResponse;
import com.project.expensemanager.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Map entity to response
    public CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getCategoryName());
    }

    // Map request to entity
    public Category mapToEntity(CategoryResponse request) {
        return new Category(request.categoryName());
    }
}
