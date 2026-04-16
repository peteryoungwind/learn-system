package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.dto.ImportTaskCreateRequest;
import com.learnsystem.dto.ImportTaskItemResponse;
import com.learnsystem.dto.ImportTaskResponse;
import com.learnsystem.dto.MaterialRequest;
import com.learnsystem.dto.UploadResponse;
import com.learnsystem.entity.ImportTaskEntity;
import com.learnsystem.entity.ImportTaskItemEntity;
import com.learnsystem.mapper.ImportTaskItemMapper;
import com.learnsystem.mapper.ImportTaskMapper;
import com.learnsystem.service.ImportTaskService;
import com.learnsystem.service.MaterialService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportTaskServiceImpl implements ImportTaskService {
    private final ImportTaskMapper importTaskMapper;
    private final ImportTaskItemMapper importTaskItemMapper;
    private final MaterialService materialService;
    private final MaterialServiceImpl materialServiceImpl;

    public ImportTaskServiceImpl(ImportTaskMapper importTaskMapper,
                                 ImportTaskItemMapper importTaskItemMapper,
                                 MaterialService materialService,
                                 MaterialServiceImpl materialServiceImpl) {
        this.importTaskMapper = importTaskMapper;
        this.importTaskItemMapper = importTaskItemMapper;
        this.materialService = materialService;
        this.materialServiceImpl = materialServiceImpl;
    }

    @Override
    public List<ImportTaskResponse> list() {
        return importTaskMapper.selectList(new LambdaQueryWrapper<ImportTaskEntity>()
                        .orderByDesc(ImportTaskEntity::getCreatedAt, ImportTaskEntity::getId))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ImportTaskResponse create(Long userId, ImportTaskCreateRequest request) throws IOException {
        materialServiceImpl.validateRelations(request.getCategoryId(), request.getAlbumId());
        MultipartFile[] files = request.getFiles();
        if (files == null || files.length == 0) {
            throw new com.learnsystem.exception.BusinessException("请至少上传一个文件");
        }
        ImportTaskEntity task = new ImportTaskEntity();
        task.setImportType("BATCH_UPLOAD");
        task.setStatus("PROCESSING");
        task.setTotalCount(files.length);
        task.setSuccessCount(0);
        task.setFailCount(0);
        task.setCategoryId(request.getCategoryId());
        task.setAlbumId(request.getAlbumId());
        task.setTaskName(StringUtils.hasText(request.getTaskName()) ? request.getTaskName() : "批量导入");
        task.setCreatedBy(userId);
        importTaskMapper.insert(task);

        List<String> errors = new ArrayList<>();
        int success = 0;
        int fail = 0;
        for (MultipartFile file : files) {
            ImportTaskItemEntity item = new ImportTaskItemEntity();
            item.setTaskId(task.getId());
            item.setOriginalFilename(file.getOriginalFilename());
            item.setFileSize(file.getSize());
            item.setStatus("PROCESSING");
            importTaskItemMapper.insert(item);
            try {
                UploadResponse upload = materialService.uploadMaterial(file);
                MaterialRequest materialRequest = new MaterialRequest();
                String originalFilename = file.getOriginalFilename();
                materialRequest.setTitle(originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(0, originalFilename.lastIndexOf('.')) : originalFilename);
                materialRequest.setAuthor("系统导入");
                materialRequest.setFileType(upload.getFileType());
                materialRequest.setObjectKey(upload.getObjectKey());
                materialRequest.setOriginalFilename(upload.getOriginalFilename());
                materialRequest.setMimeType(file.getContentType());
                materialRequest.setPreviewObjectKey(upload.getPreviewObjectKey());
                materialRequest.setPreviewStatus(upload.getPreviewStatus());
                materialRequest.setCategoryId(request.getCategoryId());
                materialRequest.setAlbumId(request.getAlbumId());
                materialRequest.setFileSize(upload.getFileSize());
                materialRequest.setPublishStatus(StringUtils.hasText(request.getPublishStatus()) ? request.getPublishStatus() : "DRAFT");
                var created = materialService.create(materialRequest);

                item.setFileType(upload.getFileType());
                item.setStoragePath(upload.getObjectKey());
                item.setMaterialId(created.getId());
                item.setStatus("SUCCESS");
                importTaskItemMapper.updateById(item);
                success++;
            } catch (Exception ex) {
                item.setStatus("FAILED");
                item.setErrorMessage(ex.getMessage());
                importTaskItemMapper.updateById(item);
                errors.add((file.getOriginalFilename() == null ? "未知文件" : file.getOriginalFilename()) + ": " + ex.getMessage());
                fail++;
            }
        }
        task.setSuccessCount(success);
        task.setFailCount(fail);
        task.setStatus(fail == 0 ? "SUCCESS" : (success > 0 ? "PARTIAL_SUCCESS" : "FAILED"));
        task.setErrorSummary(errors.isEmpty() ? null : String.join("; ", errors));
        importTaskMapper.updateById(task);
        return toResponse(task);
    }

    @Override
    public List<ImportTaskItemResponse> items(Long taskId) {
        return importTaskItemMapper.selectList(new LambdaQueryWrapper<ImportTaskItemEntity>()
                        .eq(ImportTaskItemEntity::getTaskId, taskId)
                        .orderByAsc(ImportTaskItemEntity::getId))
                .stream()
                .map(item -> ImportTaskItemResponse.builder()
                        .id(item.getId())
                        .taskId(item.getTaskId())
                        .originalFilename(item.getOriginalFilename())
                        .fileType(item.getFileType())
                        .fileSize(item.getFileSize())
                        .storagePath(item.getStoragePath())
                        .status(item.getStatus())
                        .errorMessage(item.getErrorMessage())
                        .materialId(item.getMaterialId())
                        .createdAt(item.getCreatedAt())
                        .updatedAt(item.getUpdatedAt())
                        .build())
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
                .categoryId(entity.getCategoryId())
                .albumId(entity.getAlbumId())
                .taskName(entity.getTaskName())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
