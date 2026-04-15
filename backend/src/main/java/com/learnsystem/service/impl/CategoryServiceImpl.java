package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.dto.CategoryRequest;
import com.learnsystem.dto.CategoryResponse;
import com.learnsystem.entity.CategoryEntity;
import com.learnsystem.entity.UserCategoryPermissionEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.mapper.AlbumMapper;
import com.learnsystem.mapper.CategoryMapper;
import com.learnsystem.mapper.MaterialMapper;
import com.learnsystem.mapper.UserCategoryPermissionMapper;
import com.learnsystem.service.CategoryService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final AlbumMapper albumMapper;
    private final MaterialMapper materialMapper;
    private final UserCategoryPermissionMapper permissionMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper,
                               AlbumMapper albumMapper,
                               MaterialMapper materialMapper,
                               UserCategoryPermissionMapper permissionMapper) {
        this.categoryMapper = categoryMapper;
        this.albumMapper = albumMapper;
        this.materialMapper = materialMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<CategoryResponse> listAll() {
        return categoryMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().orderByAsc(CategoryEntity::getSortOrder, CategoryEntity::getId))
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<CategoryResponse> listAuthorized(Long userId) {
        List<Long> categoryIds = permissionMapper.selectList(new LambdaQueryWrapper<UserCategoryPermissionEntity>()
                        .eq(UserCategoryPermissionEntity::getUserId, userId))
                .stream().map(UserCategoryPermissionEntity::getCategoryId).toList();
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        return categoryMapper.selectList(new LambdaQueryWrapper<CategoryEntity>()
                        .in(CategoryEntity::getId, categoryIds)
                        .eq(CategoryEntity::getStatus, "ACTIVE")
                        .orderByAsc(CategoryEntity::getSortOrder, CategoryEntity::getId))
                .stream().map(this::toResponse).toList();
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        checkCodeUnique(null, request.getCode());
        CategoryEntity entity = new CategoryEntity();
        fillEntity(entity, request);
        categoryMapper.insert(entity);
        return toResponse(entity);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        CategoryEntity entity = requireCategory(id);
        checkCodeUnique(id, request.getCode());
        fillEntity(entity, request);
        categoryMapper.updateById(entity);
        return toResponse(entity);
    }

    @Override
    public void delete(Long id) {
        CategoryEntity entity = requireCategory(id);
        Long albumCount = albumMapper.selectCount(new LambdaQueryWrapper<com.learnsystem.entity.AlbumEntity>()
                .eq(com.learnsystem.entity.AlbumEntity::getCategoryId, id));
        Long materialCount = materialMapper.selectCount(new LambdaQueryWrapper<com.learnsystem.entity.MaterialEntity>()
                .eq(com.learnsystem.entity.MaterialEntity::getCategoryId, id));
        if ((albumCount != null && albumCount > 0) || (materialCount != null && materialCount > 0)) {
            throw new BusinessException("分类下仍有关联专辑或资料，无法删除");
        }
        permissionMapper.delete(new LambdaQueryWrapper<UserCategoryPermissionEntity>().eq(UserCategoryPermissionEntity::getCategoryId, id));
        categoryMapper.deleteById(entity.getId());
    }

    private void fillEntity(CategoryEntity entity, CategoryRequest request) {
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        entity.setStatus(request.getStatus());
    }

    private void checkCodeUnique(Long id, String code) {
        LambdaQueryWrapper<CategoryEntity> wrapper = new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCode, code);
        if (id != null) {
            wrapper.ne(CategoryEntity::getId, id);
        }
        Long count = categoryMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BusinessException("分类编码已存在");
        }
    }

    private CategoryEntity requireCategory(Long id) {
        CategoryEntity entity = categoryMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("分类不存在");
        }
        return entity;
    }

    private CategoryResponse toResponse(CategoryEntity entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .status(entity.getStatus())
                .build();
    }
}
