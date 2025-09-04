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
    @Transactional()
    public UserResponseDto getUserById(Long id) {
        System.out.println(id);
        User user= userRepository.findByIdAndNotDeleted(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        return mapToResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto updateDto) {
        User user = userRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

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
