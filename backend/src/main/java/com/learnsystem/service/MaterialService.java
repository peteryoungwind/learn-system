package com.learnsystem.service;

import com.learnsystem.dto.MaterialPreviewResponse;
import com.learnsystem.dto.MaterialRequest;
import com.learnsystem.dto.MaterialResponse;
import com.learnsystem.dto.UploadResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MaterialService {
    List<MaterialResponse> adminList();

    MaterialResponse create(MaterialRequest request);

    MaterialResponse update(Long id, MaterialRequest request);

    void delete(Long id);

    List<MaterialResponse> authorizedMaterials(Long userId, Long categoryId, Long albumId, String keyword);

    MaterialResponse detail(Long userId, Long materialId);

    MaterialPreviewResponse preview(Long userId, Long materialId);

    UploadResponse uploadImage(MultipartFile file) throws IOException;

    UploadResponse uploadMaterial(MultipartFile file) throws IOException;
}
