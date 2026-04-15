package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.dto.AlbumRequest;
import com.learnsystem.dto.AlbumResponse;
import com.learnsystem.entity.AlbumEntity;
import com.learnsystem.entity.CategoryEntity;
import com.learnsystem.entity.UserCategoryPermissionEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.mapper.AlbumMapper;
import com.learnsystem.mapper.CategoryMapper;
import com.learnsystem.mapper.MaterialMapper;
import com.learnsystem.mapper.UserCategoryPermissionMapper;
import com.learnsystem.service.AlbumService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AlbumServiceImpl implements AlbumService {
    private final AlbumMapper albumMapper;
    private final CategoryMapper categoryMapper;
    private final MaterialMapper materialMapper;
    private final UserCategoryPermissionMapper permissionMapper;

    public AlbumServiceImpl(AlbumMapper albumMapper,
                            CategoryMapper categoryMapper,
                            MaterialMapper materialMapper,
                            UserCategoryPermissionMapper permissionMapper) {
        this.albumMapper = albumMapper;
        this.categoryMapper = categoryMapper;
        this.materialMapper = materialMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<AlbumResponse> listAll() {
        return albumMapper.selectList(new LambdaQueryWrapper<AlbumEntity>().orderByAsc(AlbumEntity::getSortOrder, AlbumEntity::getId))
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<AlbumResponse> listAuthorized(Long userId, Long categoryId) {
        List<Long> categoryIds = permissionMapper.selectList(new LambdaQueryWrapper<UserCategoryPermissionEntity>()
                        .eq(UserCategoryPermissionEntity::getUserId, userId))
                .stream().map(UserCategoryPermissionEntity::getCategoryId).toList();
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<AlbumEntity> wrapper = new LambdaQueryWrapper<AlbumEntity>()
                .in(AlbumEntity::getCategoryId, categoryIds)
                .eq(AlbumEntity::getStatus, "ACTIVE")
                .orderByAsc(AlbumEntity::getSortOrder, AlbumEntity::getId);
        if (categoryId != null) {
            wrapper.eq(AlbumEntity::getCategoryId, categoryId);
        }
        return albumMapper.selectList(wrapper).stream().map(this::toResponse).toList();
    }

    @Override
    public AlbumResponse create(AlbumRequest request) {
        requireActiveCategory(request.getCategoryId());
        ensureUniqueName(null, request.getCategoryId(), request.getName());
        AlbumEntity entity = new AlbumEntity();
        fillEntity(entity, request);
        albumMapper.insert(entity);
        return toResponse(entity);
    }

    @Override
    public AlbumResponse update(Long id, AlbumRequest request) {
        requireActiveCategory(request.getCategoryId());
        AlbumEntity entity = requireAlbum(id);
        ensureUniqueName(id, request.getCategoryId(), request.getName());
        fillEntity(entity, request);
        albumMapper.updateById(entity);
        return toResponse(entity);
    }

    @Override
    public void delete(Long id) {
        AlbumEntity entity = requireAlbum(id);
        Long materialCount = materialMapper.selectCount(new LambdaQueryWrapper<com.learnsystem.entity.MaterialEntity>()
                .eq(com.learnsystem.entity.MaterialEntity::getAlbumId, id));
        if (materialCount != null && materialCount > 0) {
            throw new BusinessException("专辑下仍有关联资料，无法删除");
        }
        albumMapper.deleteById(entity.getId());
    }

    private void fillEntity(AlbumEntity entity, AlbumRequest request) {
        entity.setCategoryId(request.getCategoryId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        entity.setStatus(request.getStatus());
    }

    private void ensureUniqueName(Long id, Long categoryId, String name) {
        LambdaQueryWrapper<AlbumEntity> wrapper = new LambdaQueryWrapper<AlbumEntity>()
                .eq(AlbumEntity::getCategoryId, categoryId)
                .eq(AlbumEntity::getName, name);
        if (id != null) {
            wrapper.ne(AlbumEntity::getId, id);
        }
        Long count = albumMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BusinessException("同一分类下专辑名称已存在");
        }
    }

    private CategoryEntity requireActiveCategory(Long categoryId) {
        CategoryEntity category = categoryMapper.selectById(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        return category;
    }

    private AlbumEntity requireAlbum(Long id) {
        AlbumEntity entity = albumMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("专辑不存在");
        }
        return entity;
    }

    private AlbumResponse toResponse(AlbumEntity entity) {
        return AlbumResponse.builder()
                .id(entity.getId())
                .categoryId(entity.getCategoryId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .status(entity.getStatus())
                .build();
    }
}
