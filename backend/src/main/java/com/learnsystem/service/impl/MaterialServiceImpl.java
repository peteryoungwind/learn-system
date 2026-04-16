package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.domain.UserRole;
import com.learnsystem.dto.MaterialPreviewResponse;
import com.learnsystem.dto.MaterialRequest;
import com.learnsystem.dto.MaterialResponse;
import com.learnsystem.dto.UploadResponse;
import com.learnsystem.entity.AlbumEntity;
import com.learnsystem.entity.FileTypePresetEntity;
import com.learnsystem.entity.MaterialEntity;
import com.learnsystem.entity.StorageConfigEntity;
import com.learnsystem.entity.UserCategoryPermissionEntity;
import com.learnsystem.entity.UserEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.mapper.AlbumMapper;
import com.learnsystem.mapper.CategoryMapper;
import com.learnsystem.mapper.MaterialMapper;
import com.learnsystem.mapper.UserCategoryPermissionMapper;
import com.learnsystem.mapper.UserMapper;
import com.learnsystem.service.DocumentPreviewConvertService;
import com.learnsystem.service.FileStorageService;
import com.learnsystem.service.FileTypePresetService;
import com.learnsystem.service.MaterialService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MaterialServiceImpl implements MaterialService {
    private static final DateTimeFormatter DATE_PATH = DateTimeFormatter.ofPattern("yyyyMM");

    private final MaterialMapper materialMapper;
    private final CategoryMapper categoryMapper;
    private final AlbumMapper albumMapper;
    private final UserMapper userMapper;
    private final UserCategoryPermissionMapper permissionMapper;
    private final StorageConfigServiceImpl storageConfigService;
    private final FileTypePresetService fileTypePresetService;
    private final FileStorageService fileStorageService;
    private final DocumentPreviewConvertService documentPreviewConvertService;
    private final String uploadTempDir;

    public MaterialServiceImpl(MaterialMapper materialMapper,
                               CategoryMapper categoryMapper,
                               AlbumMapper albumMapper,
                               UserMapper userMapper,
                               UserCategoryPermissionMapper permissionMapper,
                               StorageConfigServiceImpl storageConfigService,
                               FileTypePresetService fileTypePresetService,
                               FileStorageService fileStorageService,
                               DocumentPreviewConvertService documentPreviewConvertService,
                               @Value("${app.upload.temp-dir:${java.io.tmpdir}/learn-system}") String uploadTempDir) {
        this.materialMapper = materialMapper;
        this.categoryMapper = categoryMapper;
        this.albumMapper = albumMapper;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.storageConfigService = storageConfigService;
        this.fileTypePresetService = fileTypePresetService;
        this.fileStorageService = fileStorageService;
        this.documentPreviewConvertService = documentPreviewConvertService;
        this.uploadTempDir = uploadTempDir;
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
    public MaterialPreviewResponse preview(Long userId, Long materialId) {
        MaterialEntity material = requireMaterial(materialId);
        assertMaterialAccess(userId, material);
        FileTypePresetEntity preset = fileTypePresetService.getRequiredByCode(material.getFileType());
        String previewMode = preset.getPreviewMode();
        String previewUrl = resolvePreviewUrl(material, previewMode);
        String viewerType = "AUDIO".equals(previewMode) ? "audio" : "iframe";
        return MaterialPreviewResponse.builder()
                .viewerType(viewerType)
                .previewUrl(previewUrl)
                .downloadUrl(resolveDownloadUrl(material))
                .onlineSupported(true)
                .fallbackMessage(null)
                .previewStatus(material.getPreviewStatus())
                .build();
    }

    @Override
    public UploadResponse uploadImage(MultipartFile file) throws IOException {
        String objectKey = buildObjectKey("images", file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            FileStorageService.StoredFile stored = fileStorageService.upload(objectKey, inputStream, file.getSize(), file.getContentType());
            return UploadResponse.builder()
                    .url(stored.url())
                    .objectKey(stored.objectKey())
                    .fileSize(stored.size())
                    .originalFilename(file.getOriginalFilename())
                    .build();
        }
    }

    @Override
    public UploadResponse uploadMaterial(MultipartFile file) throws IOException {
        FileTypePresetEntity preset = fileTypePresetService.detectByFilename(file.getOriginalFilename());
        StorageConfigEntity config = storageConfigService.getRequiredDefaultEntity();
        String objectKey = buildObjectKey("materials", file.getOriginalFilename());
        String previewObjectKey = null;
        String previewStatus = "SUCCESS";
        try (InputStream inputStream = file.getInputStream()) {
            FileStorageService.StoredFile stored = fileStorageService.upload(objectKey, inputStream, file.getSize(), file.getContentType());
            if ("DOC_CONVERTED_PDF".equals(preset.getPreviewMode())) {
                File tempFile = createTempFile(file);
                File pdfFile = null;
                try {
                    file.transferTo(tempFile);
                    pdfFile = documentPreviewConvertService.convertToPdf(tempFile);
                    previewObjectKey = buildPreviewObjectKey(file.getOriginalFilename());
                    try (InputStream pdfInput = Files.newInputStream(pdfFile.toPath())) {
                        fileStorageService.upload(previewObjectKey, pdfInput, pdfFile.length(), "application/pdf");
                    }
                } catch (BusinessException ex) {
                    previewStatus = "FAILED";
                } finally {
                    tempFile.delete();
                    if (pdfFile != null) {
                        pdfFile.delete();
                    }
                }
            }
            return UploadResponse.builder()
                    .url(stored.url())
                    .objectKey(stored.objectKey())
                    .fileSize(stored.size())
                    .originalFilename(file.getOriginalFilename())
                    .fileType(preset.getCode())
                    .previewObjectKey(previewObjectKey)
                    .previewStatus(previewStatus)
                    .build();
        }
    }

    public void validateRelations(Long categoryId, Long albumId) {
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

    private void fillEntity(MaterialEntity entity, MaterialRequest request) {
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());
        entity.setFileType(request.getFileType());
        entity.setStorageProvider(storageConfigService.getRequiredDefaultEntity().getProvider());
        entity.setObjectKey(request.getObjectKey());
        entity.setOriginalFilename(request.getOriginalFilename());
        entity.setMimeType(request.getMimeType());
        entity.setPreviewObjectKey(request.getPreviewObjectKey());
        entity.setPreviewStatus(request.getPreviewStatus());
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

    private String resolvePreviewUrl(MaterialEntity material, String previewMode) {
        if ("DOC_CONVERTED_PDF".equals(previewMode)) {
            if (!"SUCCESS".equals(material.getPreviewStatus()) || !StringUtils.hasText(material.getPreviewObjectKey())) {
                throw new BusinessException("文档预览文件尚未准备完成");
            }
            return ((OssFileStorageServiceImpl) fileStorageService).resolvePublicUrl(material.getPreviewObjectKey());
        }
        return ((OssFileStorageServiceImpl) fileStorageService).resolvePublicUrl(material.getObjectKey());
    }

    private String resolveDownloadUrl(MaterialEntity material) {
        return ((OssFileStorageServiceImpl) fileStorageService).resolvePublicUrl(material.getObjectKey());
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

    private String buildObjectKey(String bizType, String filename) {
        StorageConfigEntity config = storageConfigService.getRequiredDefaultEntity();
        String basePath = StringUtils.hasText(config.getBasePath()) ? config.getBasePath().replaceAll("/$", "") : "learn-system";
        String safeName = StringUtils.hasText(filename) ? filename : UUID.randomUUID() + ".bin";
        return basePath + "/" + bizType + "/" + LocalDateTime.now().format(DATE_PATH) + "/" + UUID.randomUUID() + "-" + safeName;
    }

    private String buildPreviewObjectKey(String filename) {
        String baseName = (StringUtils.hasText(filename) ? filename : UUID.randomUUID().toString()).replaceFirst("\\.[^.]+$", "");
        return buildObjectKey("previews", baseName + ".pdf");
    }

    private File createTempFile(MultipartFile file) throws IOException {
        File dir = new File(uploadTempDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BusinessException("无法创建临时目录");
        }
        String suffix = StringUtils.hasText(file.getOriginalFilename()) && file.getOriginalFilename().contains(".")
                ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')) : ".tmp";
        return File.createTempFile("upload-", suffix, dir);
    }

    private MaterialResponse toResponse(MaterialEntity entity) {
        return MaterialResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .fileType(entity.getFileType())
                .storageProvider(entity.getStorageProvider())
                .objectKey(entity.getObjectKey())
                .originalFilename(entity.getOriginalFilename())
                .mimeType(entity.getMimeType())
                .previewObjectKey(entity.getPreviewObjectKey())
                .previewStatus(entity.getPreviewStatus())
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
