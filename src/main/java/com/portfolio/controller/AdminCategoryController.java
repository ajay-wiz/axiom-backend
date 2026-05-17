package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.entity.Category;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    @Autowired private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAll() {
        List<Category> cats = categoryRepository.findAll();
        cats.forEach(c -> c.setMediaList(null));
        return ResponseEntity.ok(ApiResponse.success(cats));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> create(@RequestBody Category category) {
        category.setSlug(category.getName().toLowerCase().replaceAll("\\s+", "-"));
        return ResponseEntity.ok(ApiResponse.success(categoryRepository.save(category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) throw new ResourceNotFoundException("Category not found");
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted", "OK"));
    }
}
