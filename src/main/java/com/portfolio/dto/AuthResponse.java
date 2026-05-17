package com.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String fullName;
    private String type = "Bearer";

    public AuthResponse(String token, String username, String fullName) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
    }
}
