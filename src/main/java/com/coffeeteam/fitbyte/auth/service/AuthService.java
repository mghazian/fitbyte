package com.coffeeteam.fitbyte.auth.service;

import com.coffeeteam.fitbyte.auth.dto.UserAuthRequest;
import com.coffeeteam.fitbyte.auth.dto.UserAuthResponse;

public interface AuthService {
    UserAuthResponse register(UserAuthRequest request);
    UserAuthResponse login(UserAuthRequest request);
}
