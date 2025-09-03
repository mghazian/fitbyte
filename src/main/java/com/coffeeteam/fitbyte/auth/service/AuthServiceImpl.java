package com.coffeeteam.fitbyte.auth.service;

import com.coffeeteam.fitbyte.auth.dto.UserAuthRequest;
import com.coffeeteam.fitbyte.auth.dto.UserAuthResponse;
import com.coffeeteam.fitbyte.auth.entity.User;
import com.coffeeteam.fitbyte.auth.repository.UserRepository;
import com.coffeeteam.fitbyte.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ValidationServiceImpl validationServiceImpl;

    public UserAuthResponse register(UserAuthRequest request) {
        validationServiceImpl.validate(request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }

        ZonedDateTime timeNow = ZonedDateTime.now();

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(timeNow);
        user.setUpdatedAt(timeNow);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new UserAuthResponse(user.getEmail(), token);
    }

    public UserAuthResponse login(UserAuthRequest request) {
        validationServiceImpl.validate(request);

        String dummyHash = "$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5Lvu8U8yIDzW2zQ5J8U1R13n3yNZa";

        Optional<User> user = userRepository.findByEmail(request.getEmail());
        String passwordToCheck = user.map(User::getPassword).orElse(dummyHash);
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), passwordToCheck);

        if (user.isEmpty() || !passwordMatches) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        }
        String token = jwtUtil.generateToken(user.get().getEmail());
        return new UserAuthResponse(user.get().getEmail(), token);
    }


}