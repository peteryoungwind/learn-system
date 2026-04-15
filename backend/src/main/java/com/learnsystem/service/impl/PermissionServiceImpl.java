package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.domain.UserRole;
import com.learnsystem.entity.UserCategoryPermissionEntity;
import com.learnsystem.entity.UserEntity;
import com.learnsystem.mapper.UserCategoryPermissionMapper;
import com.learnsystem.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final UserCategoryPermissionMapper permissionMapper;

    public PermissionServiceImpl(UserCategoryPermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public boolean canAccessCategory(UserEntity user, Long categoryId) {
        if (user == null || categoryId == null) {
            return false;
        }
        if (UserRole.ADMIN.name().equals(user.getRole())) {
            return true;
        }
        Long count = permissionMapper.selectCount(new LambdaQueryWrapper<UserCategoryPermissionEntity>()
                .eq(UserCategoryPermissionEntity::getUserId, user.getId())
                .eq(UserCategoryPermissionEntity::getCategoryId, categoryId));
        return count != null && count > 0;
    }
}
