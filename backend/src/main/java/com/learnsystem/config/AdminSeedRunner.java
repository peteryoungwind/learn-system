package com.learnsystem.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.domain.UserRole;
import com.learnsystem.domain.UserStatus;
import com.learnsystem.entity.UserEntity;
import com.learnsystem.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeedRunner implements CommandLineRunner {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminSeedRunner(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, "admin"));
        if (count != null && count > 0) {
            return;
        }
        UserEntity user = new UserEntity();
        user.setUsername("admin");
        user.setPasswordHash(passwordEncoder.encode("admin123"));
        user.setDisplayName("系统管理员");
        user.setRole(UserRole.ADMIN.name());
        user.setStatus(UserStatus.ACTIVE.name());
        userMapper.insert(user);
    }
}
