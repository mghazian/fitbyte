package com.coffeeteam.fitbyte.profileManagement.controller;

import com.coffeeteam.fitbyte.auth.security.CustomUserDetail;
import com.coffeeteam.fitbyte.auth.security.JwtUtil;
import com.coffeeteam.fitbyte.profileManagement.dto.UserResponseDto;
import com.coffeeteam.fitbyte.profileManagement.dto.UserUpdateDto;
import com.coffeeteam.fitbyte.profileManagement.exception.AuthException;
import com.coffeeteam.fitbyte.profileManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        System.out.println(customUserDetail.getUsername());
        UserResponseDto user = userService.getUserByEmail(customUserDetail.getUsername());
        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> updateCurrentUser( @AuthenticationPrincipal CustomUserDetail customUserDetail, @Valid @RequestBody UserUpdateDto updateDto) {
        UserResponseDto updatedUser = userService.updateUser(customUserDetail.getUsername(), updateDto);
        return ResponseEntity.ok(updatedUser);
    }
}