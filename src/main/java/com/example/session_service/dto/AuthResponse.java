package com.example.session_service.dto;

import lombok.*;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String message;
    private Integer status;
    private Set<TokenSet> tokenSet;
}
