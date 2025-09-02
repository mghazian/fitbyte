package com.coffeeteam.fitbyte.profileManagement.controller;

import com.coffeeteam.fitbyte.profileManagement.dto.UserResponseDto;
import com.coffeeteam.fitbyte.profileManagement.dto.UserUpdateDto;
import com.coffeeteam.fitbyte.profileManagement.exception.AuthException;
import com.coffeeteam.fitbyte.profileManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getCurrentUser(@RequestHeader("Authorization") String authorization ) {
        Long userId = extractUserIdFromToken(authorization);

        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> updateCurrentUser( @RequestHeader("Authorization") String authorization, @Valid @RequestBody UserUpdateDto updateDto) {
        Long userId = extractUserIdFromToken(authorization);

        UserResponseDto updatedUser = userService.updateUser(userId, updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    // Helper method to extract user ID from JWT token
    private Long extractUserIdFromToken(String authorization) {
        System.out.println("asdasdasd"+ authorization);
        if(authorization != null && authorization.startsWith("Bearer ")) {
            throw new AuthException("Bearer token expired");
        }
        String token = authorization.replace("bearer ", "").replace("Bearer ", "");

        // TODO: Implement actual JWT token parsing logic here
        // This is a placeholder - you'll need to implement JWT parsing
        // based on your authentication mechanism

        // For now, returning a mock user ID
        // In real implementation, decode JWT and extract user ID
        return 1L; // Replace with actual JWT parsing logic
    }
}