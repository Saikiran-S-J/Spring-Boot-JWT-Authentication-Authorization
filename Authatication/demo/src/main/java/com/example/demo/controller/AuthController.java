package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repo.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtil, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByUsername(req.username).isPresent())
            return ResponseEntity.badRequest().body("Username already taken");

        User u = new User();
        u.setUsername(req.username);
        u.setPassword(encoder.encode(req.password));
        u.setRoles(req.roles == null ? Set.of(Role.ROLE_USER)
                : req.roles.stream().map(Role::valueOf).collect(Collectors.toSet()));
        userRepository.save(u);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username, req.password));
        var user = userRepository.findByUsername(req.username).orElseThrow();
        var roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String token = jwtUtil.generateToken(user.getUsername(), roles);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    public static class RegisterRequest {
        public String username;
        public String password;
        public Set<String> roles;
    }

    public static class AuthRequest {
        public String username;
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public AuthResponse(String token) { this.token = token; }
    }
}
