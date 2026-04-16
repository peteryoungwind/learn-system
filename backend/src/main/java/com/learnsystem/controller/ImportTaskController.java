package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.ImportTaskCreateRequest;
import com.learnsystem.dto.ImportTaskItemResponse;
import com.learnsystem.dto.ImportTaskResponse;
import com.learnsystem.security.SecurityUtils;
import com.learnsystem.service.ImportTaskService;
import java.io.IOException;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/import-tasks")
@PreAuthorize("hasRole('ADMIN')")
public class ImportTaskController {
    private final ImportTaskService importTaskService;

    public ImportTaskController(ImportTaskService importTaskService) {
        this.importTaskService = importTaskService;
    }

    @GetMapping
    public ApiResponse<List<ImportTaskResponse>> list() {
        return ApiResponse.success(importTaskService.list());
    }

    @PostMapping
    public ApiResponse<ImportTaskResponse> create(@ModelAttribute ImportTaskCreateRequest request) throws IOException {
        return ApiResponse.success(importTaskService.create(SecurityUtils.currentUserId(), request));
    }

    @GetMapping("/{id}/items")
    public ApiResponse<List<ImportTaskItemResponse>> items(@PathVariable Long id) {
        return ApiResponse.success(importTaskService.items(id));
    }
}
