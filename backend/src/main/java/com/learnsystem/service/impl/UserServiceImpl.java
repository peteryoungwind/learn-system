package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.domain.UserRole;
import com.learnsystem.domain.UserStatus;
import com.learnsystem.dto.CreateUserRequest;
import com.learnsystem.dto.UpdateUserPermissionsRequest;
import com.learnsystem.dto.UpdateUserRequest;
import com.learnsystem.dto.UpdateUserStatusRequest;
import com.learnsystem.dto.UserProfileResponse;
import com.learnsystem.entity.UserCategoryPermissionEntity;
import com.learnsystem.entity.UserEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.mapper.CategoryMapper;
import com.learnsystem.mapper.UserCategoryPermissionMapper;
import com.learnsystem.mapper.UserMapper;
import com.learnsystem.service.UserService;
import java.util.Collections;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserCategoryPermissionMapper permissionMapper;
    private final CategoryMapper categoryMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper,
                           UserCategoryPermissionMapper permissionMapper,
                           CategoryMapper categoryMapper,
                           PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.categoryMapper = categoryMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserProfileResponse> listUsers() {
        return userMapper.selectList(new LambdaQueryWrapper<UserEntity>().orderByDesc(UserEntity::getId))
                .stream()
                .map(this::toProfile)
                .toList();
    }

    @Override
    public UserProfileResponse createUser(CreateUserRequest request) {
        validateRole(request.getRole());
        validateStatus(request.getStatus());
        Long count = userMapper.selectCount(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, request.getUsername()));
        if (count != null && count > 0) {
            throw new BusinessException("用户名已存在");
        }
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        user.setRemark(request.getRemark());
        userMapper.insert(user);
        return toProfile(user);
    }

    @Override
    public UserProfileResponse updateUser(Long userId, UpdateUserRequest request) {
        validateRole(request.getRole());
        validateStatus(request.getStatus());
        UserEntity user = requireUser(userId);
        user.setDisplayName(request.getDisplayName());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        user.setRemark(request.getRemark());
        userMapper.updateById(user);
        return toProfile(user);
    }

    @Override
    public UserProfileResponse updateStatus(Long userId, UpdateUserStatusRequest request) {
        validateStatus(request.getStatus());
        UserEntity user = requireUser(userId);
        user.setStatus(request.getStatus());
        userMapper.updateById(user);
        return toProfile(user);
    }

    @Override
    @Transactional
    public void updatePermissions(Long userId, UpdateUserPermissionsRequest request, Long operatorId) {
        requireUser(userId);
        List<Long> categoryIds = request.getCategoryIds() == null ? Collections.emptyList() : request.getCategoryIds();
        if (!categoryIds.isEmpty()) {
            Long validCount = categoryMapper.selectCount(new LambdaQueryWrapper<com.learnsystem.entity.CategoryEntity>()
                    .in(com.learnsystem.entity.CategoryEntity::getId, categoryIds));
            if (validCount == null || validCount.intValue() != categoryIds.size()) {
                throw new BusinessException("存在无效分类");
            }
        }
        permissionMapper.delete(new LambdaQueryWrapper<UserCategoryPermissionEntity>()
                .eq(UserCategoryPermissionEntity::getUserId, userId));
        for (Long categoryId : categoryIds) {
            UserCategoryPermissionEntity permission = new UserCategoryPermissionEntity();
            permission.setUserId(userId);
            permission.setCategoryId(categoryId);
            permission.setCreatedBy(operatorId);
            permissionMapper.insert(permission);
        }
    }

    private UserEntity requireUser(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    private UserProfileResponse toProfile(UserEntity user) {
        List<Long> categoryIds = permissionMapper.selectList(new LambdaQueryWrapper<UserCategoryPermissionEntity>()
                        .eq(UserCategoryPermissionEntity::getUserId, user.getId()))
                .stream()
                .map(UserCategoryPermissionEntity::getCategoryId)
                .toList();
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .role(user.getRole())
                .status(user.getStatus())
                .categoryIds(categoryIds)
                .build();
    }

    private void validateRole(String role) {
        if (!UserRole.ADMIN.name().equals(role) && !UserRole.USER.name().equals(role)) {
            throw new BusinessException("无效角色");
        }
    }

    private void validateStatus(String status) {
        if (!UserStatus.INACTIVE.name().equals(status)
                && !UserStatus.ACTIVE.name().equals(status)
                && !UserStatus.DISABLED.name().equals(status)) {
            throw new BusinessException("无效状态");
        }
    }
}
