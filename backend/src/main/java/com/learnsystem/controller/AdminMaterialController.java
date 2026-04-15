package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.MaterialRequest;
import com.learnsystem.dto.MaterialResponse;
import com.learnsystem.service.MaterialService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/materials")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMaterialController {
    private final MaterialService materialService;

    public AdminMaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public ApiResponse<List<MaterialResponse>> list() {
        return ApiResponse.success(materialService.adminList());
    }

    @PostMapping
    public ApiResponse<MaterialResponse> create(@Valid @RequestBody MaterialRequest request) {
        return ApiResponse.success(materialService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<MaterialResponse> update(@PathVariable Long id, @Valid @RequestBody MaterialRequest request) {
        return ApiResponse.success(materialService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ApiResponse.successMessage("删除成功");
    }
}
