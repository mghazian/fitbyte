package com.coffeeteam.fitbyte.auth.controller;

import com.coffeeteam.fitbyte.auth.dto.UserAuthRequest;
import com.coffeeteam.fitbyte.auth.dto.UserAuthResponse;
import com.coffeeteam.fitbyte.auth.service.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuthResponse> register(@Valid @RequestBody UserAuthRequest request) {

        UserAuthResponse response = this.authServiceImpl.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@Valid @RequestBody UserAuthRequest request) {
        UserAuthResponse response = this.authServiceImpl.login(request);
        return ResponseEntity.ok(response);
    }
}
