package com.taskflow.taskflow_api.controller;

import com.taskflow.taskflow_api.dto.AuthRequest;
import com.taskflow.taskflow_api.entity.User;
import com.taskflow.taskflow_api.repository.UserRepository;
import com.taskflow.taskflow_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already in use"));
        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName() != null ? req.getFullName() : "TaskFlow User")
                .build();
        userRepository.save(user);
        return ResponseEntity.ok(Map.of(
                "token", jwtUtil.generateToken(user.getEmail()),
                "email", user.getEmail(),
                "name", user.getFullName()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest req) {
        return userRepository.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .map(u -> ResponseEntity.ok(Map.of(
                        "token", jwtUtil.generateToken(u.getEmail()),
                        "email", u.getEmail(),
                        "name", u.getFullName())))
                .orElse(ResponseEntity.status(401)
                        .body(Map.of("error", "Invalid credentials")));
    }
}
