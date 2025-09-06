package com.coffeeteam.fitbyte.profileManagement.service;

import com.coffeeteam.fitbyte.core.entity.User;
import com.coffeeteam.fitbyte.core.repository.UserRepository;
import com.coffeeteam.fitbyte.profileManagement.dto.UserResponseDto;
import com.coffeeteam.fitbyte.profileManagement.dto.UserUpdateDto;
import com.coffeeteam.fitbyte.profileManagement.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user= userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        return mapToResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(String email, UserUpdateDto updateDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Update only non-null fields
        if (updateDto.getPreference() != null) {
            user.setPreference(updateDto.getPreference());
        }
        if (updateDto.getWeightUnit() != null) {
            user.setWeightUnit(updateDto.getWeightUnit());
        }
        if (updateDto.getHeightUnit() != null) {
            user.setHeightUnit(updateDto.getHeightUnit());
        }
        if (updateDto.getWeight() != null) {
            user.setWeight(updateDto.getWeight());
        }
        if (updateDto.getHeight() != null) {
            user.setHeight(updateDto.getHeight());
        }
        if (updateDto.getName() != null) {
            user.setName(updateDto.getName());
        }
        if (updateDto.getImageUri() != null) {
            user.setImageUri(updateDto.getImageUri());
        }

        User savedUser = userRepository.save(user);
        return mapToResponseDto(savedUser);
    }

    private UserResponseDto mapToResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setPreference(user.getPreference());
        dto.setWeightUnit(user.getWeightUnit());
        dto.setHeightUnit(user.getHeightUnit());
        dto.setWeight(user.getWeight());
        dto.setHeight(user.getHeight());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setImageUri(user.getImageUri());
        return dto;
    }
}
