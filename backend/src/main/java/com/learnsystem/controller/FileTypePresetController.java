package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.FileTypePresetResponse;
import com.learnsystem.service.FileTypePresetService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/file-types")
@PreAuthorize("hasRole('ADMIN')")
public class FileTypePresetController {
    private final FileTypePresetService fileTypePresetService;

    public FileTypePresetController(FileTypePresetService fileTypePresetService) {
        this.fileTypePresetService = fileTypePresetService;
    }

    @GetMapping
    public ApiResponse<List<FileTypePresetResponse>> list() {
        return ApiResponse.success(fileTypePresetService.listEnabled());
    }
}
