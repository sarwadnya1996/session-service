package com.example.session_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AuthRequest {
    private String userName;
    private String password;

}
