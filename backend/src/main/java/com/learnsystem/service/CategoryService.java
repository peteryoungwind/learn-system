package com.learnsystem.service;

import com.learnsystem.dto.CategoryRequest;
import com.learnsystem.dto.CategoryResponse;
import java.util.List;

public interface CategoryService {
    List<CategoryResponse> listAll();

    List<CategoryResponse> listAuthorized(Long userId);

    List<CategoryResponse> listUserVisible(Long userId);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
