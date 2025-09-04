package com.coffeeteam.fitbyte.profileManagement.service;


import com.coffeeteam.fitbyte.profileManagement.dto.UserResponseDto;
import com.coffeeteam.fitbyte.profileManagement.dto.UserUpdateDto;

public interface UserService {
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(Long id, UserUpdateDto updateDto);
}