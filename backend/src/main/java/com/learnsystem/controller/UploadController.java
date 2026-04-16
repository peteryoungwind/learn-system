package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.UploadResponse;
import com.learnsystem.service.MaterialService;
import java.io.IOException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/uploads")
@PreAuthorize("hasRole('ADMIN')")
public class UploadController {
    private final MaterialService materialService;

    public UploadController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/image")
    public ApiResponse<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success(materialService.uploadImage(file));
    }

    @PostMapping("/material")
    public ApiResponse<UploadResponse> uploadMaterial(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success(materialService.uploadMaterial(file));
    }
}
