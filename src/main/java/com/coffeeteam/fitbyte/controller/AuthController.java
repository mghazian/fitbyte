package com.coffeeteam.fitbyte.controller;

import com.coffeeteam.fitbyte.dto.UserAuthRequest;
import com.coffeeteam.fitbyte.dto.UserAuthResponse;
import com.coffeeteam.fitbyte.entity.User;
import com.coffeeteam.fitbyte.repository.UserRepository;
import com.coffeeteam.fitbyte.service.AuthService;
import com.coffeeteam.fitbyte.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuthResponse> register(@RequestBody UserAuthRequest request) {

        UserAuthResponse response = this.authService.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody UserAuthRequest request) {
        UserAuthResponse response = this.authService.login(request);
        return ResponseEntity.ok(response);
    }
}
