package com.learnsystem.service;

import com.learnsystem.dto.FileTypePresetResponse;
import com.learnsystem.entity.FileTypePresetEntity;
import java.util.List;

public interface FileTypePresetService {
    List<FileTypePresetResponse> listEnabled();

    FileTypePresetEntity detectByFilename(String filename);

    FileTypePresetEntity getRequiredByCode(String code);
}
