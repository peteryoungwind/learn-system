package com.learnsystem.service;

import com.learnsystem.entity.UserEntity;

public interface PermissionService {
    boolean canAccessCategory(UserEntity user, Long categoryId);
}
