package com.example.session_service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TokenSet {
    private String tokenName;
    private String tokenValue;
}
