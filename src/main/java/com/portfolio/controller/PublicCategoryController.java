package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.entity.Category;
import com.portfolio.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/public/categories")
public class PublicCategoryController {
    @Autowired private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAll() {
        List<Category> cats = categoryRepository.findAll();
        cats.forEach(c -> c.setMediaList(null));
        return ResponseEntity.ok(ApiResponse.success(cats));
    }
}
