package com.coffeeteam.fitbyte.auth.controller;

import com.coffeeteam.fitbyte.auth.dto.UserAuthRequest;
import com.coffeeteam.fitbyte.auth.dto.UserAuthResponse;
import com.coffeeteam.fitbyte.auth.service.AuthServiceImpl;
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
    public ResponseEntity<UserAuthResponse> register(@RequestBody UserAuthRequest request) {

        UserAuthResponse response = this.authServiceImpl.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody UserAuthRequest request) {
        UserAuthResponse response = this.authServiceImpl.login(request);
        return ResponseEntity.ok(response);
    }
}
