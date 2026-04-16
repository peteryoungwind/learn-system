package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.dto.FileTypePresetResponse;
import com.learnsystem.entity.FileTypePresetEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.mapper.FileTypePresetMapper;
import com.learnsystem.service.FileTypePresetService;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class FileTypePresetServiceImpl implements FileTypePresetService {
    private final FileTypePresetMapper fileTypePresetMapper;

    public FileTypePresetServiceImpl(FileTypePresetMapper fileTypePresetMapper) {
        this.fileTypePresetMapper = fileTypePresetMapper;
    }

    @Override
    public List<FileTypePresetResponse> listEnabled() {
        return fileTypePresetMapper.selectList(new LambdaQueryWrapper<FileTypePresetEntity>()
                        .eq(FileTypePresetEntity::getIsEnabled, true)
                        .orderByAsc(FileTypePresetEntity::getSortOrder, FileTypePresetEntity::getId))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FileTypePresetEntity detectByFilename(String filename) {
        String extension = getExtension(filename);
        if (!StringUtils.hasText(extension)) {
            throw new BusinessException("无法识别文件类型");
        }
        List<FileTypePresetEntity> presets = fileTypePresetMapper.selectList(new LambdaQueryWrapper<FileTypePresetEntity>()
                .eq(FileTypePresetEntity::getIsEnabled, true));
        return presets.stream()
                .filter(item -> Arrays.stream(item.getExtensions().split(","))
                        .map(String::trim)
                        .map(value -> value.toLowerCase(Locale.ROOT))
                        .anyMatch(value -> value.equals(extension)))
                .findFirst()
                .orElseThrow(() -> new BusinessException("不支持的文件类型: " + extension));
    }

    @Override
    public FileTypePresetEntity getRequiredByCode(String code) {
        FileTypePresetEntity entity = fileTypePresetMapper.selectOne(new LambdaQueryWrapper<FileTypePresetEntity>()
                .eq(FileTypePresetEntity::getCode, code)
                .eq(FileTypePresetEntity::getIsEnabled, true)
                .last("LIMIT 1"));
        if (entity == null) {
            throw new BusinessException("文件类型不存在: " + code);
        }
        return entity;
    }

    private String getExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private FileTypePresetResponse toResponse(FileTypePresetEntity entity) {
        return FileTypePresetResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .extensions(entity.getExtensions())
                .mimeTypes(entity.getMimeTypes())
                .previewMode(entity.getPreviewMode())
                .enabled(entity.getIsEnabled())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
