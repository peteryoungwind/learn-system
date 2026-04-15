package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.domain.UserRole;
import com.learnsystem.dto.MaterialRequest;
import com.learnsystem.dto.MaterialResponse;
import com.learnsystem.entity.AlbumEntity;
import com.learnsystem.entity.MaterialEntity;
import com.learnsystem.entity.UserCategoryPermissionEntity;
import com.learnsystem.entity.UserEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.mapper.AlbumMapper;
import com.learnsystem.mapper.CategoryMapper;
import com.learnsystem.mapper.MaterialMapper;
import com.learnsystem.mapper.UserCategoryPermissionMapper;
import com.learnsystem.mapper.UserMapper;
import com.learnsystem.service.MaterialService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MaterialServiceImpl implements MaterialService {
    private final MaterialMapper materialMapper;
    private final CategoryMapper categoryMapper;
    private final AlbumMapper albumMapper;
    private final UserMapper userMapper;
    private final UserCategoryPermissionMapper permissionMapper;

    public MaterialServiceImpl(MaterialMapper materialMapper,
                               CategoryMapper categoryMapper,
                               AlbumMapper albumMapper,
                               UserMapper userMapper,
                               UserCategoryPermissionMapper permissionMapper) {
        this.materialMapper = materialMapper;
        this.categoryMapper = categoryMapper;
        this.albumMapper = albumMapper;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<MaterialResponse> adminList() {
        return materialMapper.selectList(new LambdaQueryWrapper<MaterialEntity>().orderByDesc(MaterialEntity::getId))
                .stream().map(this::toResponse).toList();
    }

    @Override
    public MaterialResponse create(MaterialRequest request) {
        validateRelations(request.getCategoryId(), request.getAlbumId());
        MaterialEntity entity = new MaterialEntity();
        fillEntity(entity, request);
        materialMapper.insert(entity);
        return toResponse(entity);
    }

    @Override
    public MaterialResponse update(Long id, MaterialRequest request) {
        MaterialEntity entity = requireMaterial(id);
        validateRelations(request.getCategoryId(), request.getAlbumId());
        fillEntity(entity, request);
        materialMapper.updateById(entity);
        return toResponse(entity);
    }

    @Override
    public void delete(Long id) {
        requireMaterial(id);
        materialMapper.deleteById(id);
    }

    @Override
    public List<MaterialResponse> authorizedMaterials(Long userId, Long categoryId, Long albumId, String keyword) {
        UserEntity user = requireUser(userId);
        LambdaQueryWrapper<MaterialEntity> wrapper = new LambdaQueryWrapper<MaterialEntity>()
                .eq(MaterialEntity::getPublishStatus, "PUBLISHED")
                .orderByDesc(MaterialEntity::getPublishTime, MaterialEntity::getId);
        if (!UserRole.ADMIN.name().equals(user.getRole())) {
            List<Long> categoryIds = permissionMapper.selectList(new LambdaQueryWrapper<UserCategoryPermissionEntity>()
                            .eq(UserCategoryPermissionEntity::getUserId, userId))
                    .stream().map(UserCategoryPermissionEntity::getCategoryId).toList();
            if (categoryIds.isEmpty()) {
                return List.of();
            }
            wrapper.in(MaterialEntity::getCategoryId, categoryIds);
        }
        if (categoryId != null) {
            wrapper.eq(MaterialEntity::getCategoryId, categoryId);
        }
        if (albumId != null) {
            wrapper.eq(MaterialEntity::getAlbumId, albumId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(q -> q.like(MaterialEntity::getTitle, keyword).or().like(MaterialEntity::getAuthor, keyword));
        }
        return materialMapper.selectList(wrapper).stream().map(this::toResponse).toList();
    }

    @Override
    public MaterialResponse detail(Long userId, Long materialId) {
        MaterialEntity material = requireMaterial(materialId);
        assertMaterialAccess(userId, material);
        return toResponse(material);
    }

    @Override
    public String previewUrl(Long userId, Long materialId) {
        MaterialEntity material = requireMaterial(materialId);
        assertMaterialAccess(userId, material);
        return "/api/me/materials/" + materialId + "/preview/content?objectKey=" + material.getObjectKey();
    }

    private void fillEntity(MaterialEntity entity, MaterialRequest request) {
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());
        entity.setFileType(request.getFileType());
        entity.setStorageProvider(request.getStorageProvider());
        entity.setObjectKey(request.getObjectKey());
        entity.setCategoryId(request.getCategoryId());
        entity.setAlbumId(request.getAlbumId());
        entity.setSubtitle(request.getSubtitle());
        entity.setSummary(request.getSummary());
        entity.setCoverUrl(request.getCoverUrl());
        entity.setFileSize(request.getFileSize());
        entity.setTags(request.getTags());
        entity.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        entity.setRemark(request.getRemark());
        entity.setPublishStatus(request.getPublishStatus());
        entity.setPublishTime("PUBLISHED".equals(request.getPublishStatus()) ? LocalDateTime.now() : null);
        if (entity.getIngestTime() == null) {
            entity.setIngestTime(LocalDateTime.now());
        }
    }

    private void validateRelations(Long categoryId, Long albumId) {
        if (categoryMapper.selectById(categoryId) == null) {
            throw new BusinessException("分类不存在");
        }
        if (albumId != null) {
            AlbumEntity album = albumMapper.selectById(albumId);
            if (album == null) {
                throw new BusinessException("专辑不存在");
            }
            if (!album.getCategoryId().equals(categoryId)) {
                throw new BusinessException("专辑与分类不匹配");
            }
        }
    }

    private void assertMaterialAccess(Long userId, MaterialEntity material) {
        UserEntity user = requireUser(userId);
        if (UserRole.ADMIN.name().equals(user.getRole())) {
            return;
        }
        Long count = permissionMapper.selectCount(new LambdaQueryWrapper<UserCategoryPermissionEntity>()
                .eq(UserCategoryPermissionEntity::getUserId, userId)
                .eq(UserCategoryPermissionEntity::getCategoryId, material.getCategoryId()));
        if (count == null || count == 0 || !"PUBLISHED".equals(material.getPublishStatus())) {
            throw new BusinessException("无访问权限");
        }
    }

    private MaterialEntity requireMaterial(Long id) {
        MaterialEntity entity = materialMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("资料不存在");
        }
        return entity;
    }

    private UserEntity requireUser(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    private MaterialResponse toResponse(MaterialEntity entity) {
        return MaterialResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .fileType(entity.getFileType())
                .storageProvider(entity.getStorageProvider())
                .objectKey(entity.getObjectKey())
                .categoryId(entity.getCategoryId())
                .albumId(entity.getAlbumId())
                .subtitle(entity.getSubtitle())
                .summary(entity.getSummary())
                .coverUrl(entity.getCoverUrl())
                .fileSize(entity.getFileSize())
                .tags(entity.getTags())
                .sortOrder(entity.getSortOrder())
                .remark(entity.getRemark())
                .publishStatus(entity.getPublishStatus())
                .publishTime(entity.getPublishTime())
                .ingestTime(entity.getIngestTime())
                .build();
    }
}
