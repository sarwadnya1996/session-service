package com.example.session_service.service;

import com.example.session_service.dto.AuthRequest;
import com.example.session_service.dto.AuthResponse;
import com.example.session_service.dto.TokenDetail;
import com.example.session_service.dto.TokenSet;
import com.example.session_service.model.User;
import com.example.session_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class SessionService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse createToken(AuthRequest request) {
        log.info("validating user");


        Optional<User> optionalUser = repository.findUserByUserName(request.getUserName());

        if (optionalUser.isEmpty()) {
            log.info("User does not exist");
            throw new RuntimeException("User does not exist");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.info("Invalid credentials");
            throw new RuntimeException("Invalid credentials");
        }


        String token = getToken(request);

        return buildSuccessResponse(token);
    }

    private AuthResponse buildSuccessResponse(String token) {
        return AuthResponse.builder()
                .message("session created successfully")
                .status(Integer.valueOf(1))
                .tokenSet(Set.of(TokenSet.builder()
                        .tokenName("USER_SESSION")
                        .tokenValue(token)
                        .build()))
                .build();
    }

    @SneakyThrows
    private String getToken(AuthRequest request) {
        String sessionId = UUID.randomUUID().toString();
        long issuesAt = System.currentTimeMillis();
        TokenDetail detail = TokenDetail.builder()
                .sessionId(sessionId)
                .issuedAt(Long.toString(issuesAt))
                .authRequest(request)
                .expireAt(String.valueOf((issuesAt + (15 * 60 * 1000)))).build();

        String payload = new ObjectMapper().writeValueAsString(detail);

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        byte[] iv =new byte[16];
        SecretKey key= keyGen.generateKey();

        IvParameterSpec parameterSpec = new IvParameterSpec(iv);

        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,key,parameterSpec);
        byte[] encrypted = cipher.doFinal(payload.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
