package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.entity.Testimonial;
import com.portfolio.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/public/testimonials")
public class PublicTestimonialController {
    @Autowired private TestimonialRepository testimonialRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Testimonial>>> getFeatured() {
        return ResponseEntity.ok(ApiResponse.success(testimonialRepository.findByIsFeaturedTrue()));
    }
}
