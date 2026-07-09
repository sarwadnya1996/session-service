package com.example.session_service.controller;

import com.example.session_service.dto.AuthRequest;
import com.example.session_service.dto.AuthResponse;
import com.example.session_service.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class SessionController {
    private final SessionService sessionService;

    @PostMapping("/createToken")
    public ResponseEntity<AuthResponse> createToken(@ModelAttribute AuthRequest request){
        log.info("creating token for user {}",request.getUserName());
        return ResponseEntity.ok(sessionService.createToken(request));

    }

}
