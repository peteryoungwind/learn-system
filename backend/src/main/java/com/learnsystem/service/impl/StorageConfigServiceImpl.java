package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.dto.StorageConfigRequest;
import com.learnsystem.dto.StorageConfigResponse;
import com.learnsystem.entity.StorageConfigEntity;
import com.learnsystem.mapper.StorageConfigMapper;
import com.learnsystem.service.StorageConfigService;
import org.springframework.stereotype.Service;

@Service
public class StorageConfigServiceImpl implements StorageConfigService {
    private final StorageConfigMapper storageConfigMapper;

    public StorageConfigServiceImpl(StorageConfigMapper storageConfigMapper) {
        this.storageConfigMapper = storageConfigMapper;
    }

    @Override
    public StorageConfigResponse get() {
        StorageConfigEntity entity = getDefaultEntity();
        return entity == null ? null : toResponse(entity);
    }

    @Override
    public StorageConfigResponse update(StorageConfigRequest request) {
        StorageConfigEntity entity = getDefaultEntity();
        if (entity == null) {
            entity = new StorageConfigEntity();
            entity.setIsDefault(true);
        }
        entity.setProvider(request.getProvider());
        entity.setEndpoint(request.getEndpoint());
        entity.setBucket(request.getBucket());
        entity.setRegion(request.getRegion());
        entity.setAccessKeyId(request.getAccessKeyId());
        entity.setAccessKeySecret(request.getAccessKeySecret());
        entity.setDomain(request.getDomain());
        entity.setBasePath(request.getBasePath());
        entity.setStatus(request.getStatus());
        if (entity.getId() == null) {
            storageConfigMapper.insert(entity);
        } else {
            storageConfigMapper.updateById(entity);
        }
        return toResponse(entity);
    }

    public StorageConfigEntity getRequiredDefaultEntity() {
        StorageConfigEntity entity = getDefaultEntity();
        if (entity == null) {
            throw new com.learnsystem.exception.BusinessException("请先配置全局存储设置");
        }
        return entity;
    }

    private StorageConfigEntity getDefaultEntity() {
        return storageConfigMapper.selectOne(new LambdaQueryWrapper<StorageConfigEntity>()
                .eq(StorageConfigEntity::getIsDefault, true)
                .last("LIMIT 1"));
    }

    private StorageConfigResponse toResponse(StorageConfigEntity entity) {
        return StorageConfigResponse.builder()
                .id(entity.getId())
                .provider(entity.getProvider())
                .endpoint(entity.getEndpoint())
                .bucket(entity.getBucket())
                .region(entity.getRegion())
                .accessKeyId(entity.getAccessKeyId())
                .accessKeySecret(entity.getAccessKeySecret())
                .domain(entity.getDomain())
                .basePath(entity.getBasePath())
                .status(entity.getStatus())
                .isDefault(entity.getIsDefault())
                .build();
    }
}
