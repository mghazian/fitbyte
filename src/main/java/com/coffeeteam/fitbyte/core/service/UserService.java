package com.coffeeteam.fitbyte.core.service;

import com.coffeeteam.fitbyte.core.entity.User;
import com.coffeeteam.fitbyte.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getByIdOrThrow(Long userId) {
        return userRepository.findById(userId.intValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    @Transactional
    public void updateImageKeyById(Long userId, String objectKey) {
        User user = getByIdOrThrow(userId);
        user.setImageUri(objectKey);
        userRepository.save(user);
    }
}
