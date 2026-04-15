package com.learnsystem.service;

import com.learnsystem.dto.MaterialRequest;
import com.learnsystem.dto.MaterialResponse;
import java.util.List;

public interface MaterialService {
    List<MaterialResponse> adminList();

    MaterialResponse create(MaterialRequest request);

    MaterialResponse update(Long id, MaterialRequest request);

    void delete(Long id);

    List<MaterialResponse> authorizedMaterials(Long userId, Long categoryId, Long albumId, String keyword);

    MaterialResponse detail(Long userId, Long materialId);

    String previewUrl(Long userId, Long materialId);
}
