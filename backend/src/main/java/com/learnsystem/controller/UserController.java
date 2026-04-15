package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.CreateUserRequest;
import com.learnsystem.dto.UpdateUserPermissionsRequest;
import com.learnsystem.dto.UpdateUserRequest;
import com.learnsystem.dto.UpdateUserStatusRequest;
import com.learnsystem.dto.UserProfileResponse;
import com.learnsystem.security.SecurityUtils;
import com.learnsystem.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<UserProfileResponse>> list() {
        return ApiResponse.success(userService.listUsers());
    }

    @PostMapping
    public ApiResponse<UserProfileResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserProfileResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return ApiResponse.success(userService.updateUser(id, request));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<UserProfileResponse> updateStatus(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateUserStatusRequest request) {
        return ApiResponse.success(userService.updateStatus(id, request));
    }

    @PutMapping("/{id}/permissions/categories")
    public ApiResponse<Void> updatePermissions(@PathVariable Long id,
                                               @Valid @RequestBody UpdateUserPermissionsRequest request) {
        userService.updatePermissions(id, request, SecurityUtils.currentUserId());
        return ApiResponse.successMessage("保存成功");
    }
}
