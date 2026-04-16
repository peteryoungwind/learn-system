package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.ImportTaskResponse;
import com.learnsystem.service.ImportTaskService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
