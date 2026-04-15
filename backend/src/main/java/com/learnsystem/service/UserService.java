package com.learnsystem.service;

import com.learnsystem.dto.CreateUserRequest;
import com.learnsystem.dto.UpdateUserPermissionsRequest;
import com.learnsystem.dto.UpdateUserRequest;
import com.learnsystem.dto.UpdateUserStatusRequest;
import com.learnsystem.dto.UserProfileResponse;
import java.util.List;

public interface UserService {
    List<UserProfileResponse> listUsers();

    UserProfileResponse createUser(CreateUserRequest request);

    UserProfileResponse updateUser(Long userId, UpdateUserRequest request);

    UserProfileResponse updateStatus(Long userId, UpdateUserStatusRequest request);

    void updatePermissions(Long userId, UpdateUserPermissionsRequest request, Long operatorId);
}
