package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.StorageConfigRequest;
import com.learnsystem.dto.StorageConfigResponse;
import com.learnsystem.service.StorageConfigService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/storage-config")
@PreAuthorize("hasRole('ADMIN')")
public class StorageConfigController {
    private final StorageConfigService storageConfigService;

    public StorageConfigController(StorageConfigService storageConfigService) {
        this.storageConfigService = storageConfigService;
    }

    @GetMapping
    public ApiResponse<StorageConfigResponse> get() {
        return ApiResponse.success(storageConfigService.get());
    }

    @PutMapping
    public ApiResponse<StorageConfigResponse> update(@Valid @RequestBody StorageConfigRequest request) {
        return ApiResponse.success(storageConfigService.update(request));
    }
}
