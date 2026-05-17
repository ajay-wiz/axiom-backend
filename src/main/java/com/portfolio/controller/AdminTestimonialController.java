package com.portfolio.controller;

import com.portfolio.dto.ApiResponse;
import com.portfolio.entity.Testimonial;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/testimonials")
public class AdminTestimonialController {
    @Autowired private TestimonialRepository testimonialRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Testimonial>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(testimonialRepository.findAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Testimonial>> create(@RequestBody Testimonial testimonial) {
        return ResponseEntity.ok(ApiResponse.success(testimonialRepository.save(testimonial)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Testimonial>> update(@PathVariable Long id, @RequestBody Testimonial data) {
        Testimonial t = testimonialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        t.setClientName(data.getClientName());
        t.setClientTitle(data.getClientTitle());
        t.setClientCompany(data.getClientCompany());
        t.setContent(data.getContent());
        t.setRating(data.getRating());
        t.setIsFeatured(data.getIsFeatured());
        return ResponseEntity.ok(ApiResponse.success(testimonialRepository.save(t)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        if (!testimonialRepository.existsById(id)) throw new ResourceNotFoundException("Not found");
        testimonialRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted", "OK"));
    }
}
