package com.learnsystem.service;

import com.learnsystem.dto.LoginRequest;
import com.learnsystem.dto.LoginResponse;
import com.learnsystem.dto.UserProfileResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    UserProfileResponse currentUser(Long userId);
}
