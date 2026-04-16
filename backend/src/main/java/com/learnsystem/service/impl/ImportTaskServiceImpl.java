package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.dto.ImportTaskResponse;
import com.learnsystem.entity.ImportTaskEntity;
import com.learnsystem.mapper.ImportTaskMapper;
import com.learnsystem.service.ImportTaskService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ImportTaskServiceImpl implements ImportTaskService {
    private final ImportTaskMapper importTaskMapper;

    public ImportTaskServiceImpl(ImportTaskMapper importTaskMapper) {
        this.importTaskMapper = importTaskMapper;
    }

    @Override
    public List<ImportTaskResponse> list() {
        return importTaskMapper.selectList(new LambdaQueryWrapper<ImportTaskEntity>()
                        .orderByDesc(ImportTaskEntity::getCreatedAt, ImportTaskEntity::getId))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ImportTaskResponse toResponse(ImportTaskEntity entity) {
        return ImportTaskResponse.builder()
                .id(entity.getId())
                .importType(entity.getImportType())
                .status(entity.getStatus())
                .totalCount(entity.getTotalCount())
                .successCount(entity.getSuccessCount())
                .failCount(entity.getFailCount())
                .errorSummary(entity.getErrorSummary())
                .sourceProvider(entity.getSourceProvider())
                .sourceBucket(entity.getSourceBucket())
                .sourcePrefix(entity.getSourcePrefix())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
