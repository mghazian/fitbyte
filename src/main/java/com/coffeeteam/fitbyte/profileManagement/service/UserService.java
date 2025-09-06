package com.coffeeteam.fitbyte.profileManagement.service;


import com.coffeeteam.fitbyte.profileManagement.dto.UserResponseDto;
import com.coffeeteam.fitbyte.profileManagement.dto.UserUpdateDto;

public interface UserService {
    UserResponseDto getUserByEmail(String email);
    UserResponseDto updateUser(String email, UserUpdateDto updateDto);
}