package com.portfolio.controller;

import com.portfolio.dto.*;
import com.portfolio.entity.Admin;
import com.portfolio.repository.AdminRepository;
import com.portfolio.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/admin/auth")
public class AuthController {

    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private AdminRepository adminRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        String token = tokenProvider.generateToken(auth.getName());
        Admin admin = adminRepository.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(ApiResponse.success(new AuthResponse(token, admin.getUsername(), admin.getFullName())));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify() {
        return ResponseEntity.ok(ApiResponse.success("Token is valid", "OK"));
    }
}
