package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.domain.UserStatus;
import com.learnsystem.dto.LoginRequest;
import com.learnsystem.dto.LoginResponse;
import com.learnsystem.dto.UserProfileResponse;
import com.learnsystem.entity.UserCategoryPermissionEntity;
import com.learnsystem.entity.UserEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.mapper.UserCategoryPermissionMapper;
import com.learnsystem.mapper.UserMapper;
import com.learnsystem.security.JwtTokenProvider;
import com.learnsystem.service.AuthService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final UserCategoryPermissionMapper permissionMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserMapper userMapper,
                           UserCategoryPermissionMapper permissionMapper,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UserEntity user = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, request.getUsername()));
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (UserStatus.DISABLED.name().equals(user.getStatus())) {
            throw new BusinessException("账号已停用");
        }
        user.setLastLoginAt(LocalDateTime.now());
        userMapper.updateById(user);
        UserProfileResponse profile = buildProfile(user);
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, profile);
    }

    @Override
    public UserProfileResponse currentUser(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return buildProfile(user);
    }

    private UserProfileResponse buildProfile(UserEntity user) {
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
}
