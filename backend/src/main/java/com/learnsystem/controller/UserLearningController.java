package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.AlbumResponse;
import com.learnsystem.dto.CategoryResponse;
import com.learnsystem.dto.LearningProgressRequest;
import com.learnsystem.dto.MaterialResponse;
import com.learnsystem.security.SecurityUtils;
import com.learnsystem.service.AlbumService;
import com.learnsystem.service.CategoryService;
import com.learnsystem.service.LearningProgressService;
import com.learnsystem.service.MaterialService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class UserLearningController {
    private final CategoryService categoryService;
    private final AlbumService albumService;
    private final MaterialService materialService;
    private final LearningProgressService learningProgressService;

    public UserLearningController(CategoryService categoryService,
                                  AlbumService albumService,
                                  MaterialService materialService,
                                  LearningProgressService learningProgressService) {
        this.categoryService = categoryService;
        this.albumService = albumService;
        this.materialService = materialService;
        this.learningProgressService = learningProgressService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> categories() {
        return ApiResponse.success(categoryService.listAuthorized(SecurityUtils.currentUserId()));
    }

    @GetMapping("/albums")
    public ApiResponse<List<AlbumResponse>> albums(@RequestParam(required = false) Long categoryId) {
        return ApiResponse.success(albumService.listAuthorized(SecurityUtils.currentUserId(), categoryId));
    }

    @GetMapping("/materials")
    public ApiResponse<List<MaterialResponse>> materials(@RequestParam(required = false) Long categoryId,
                                                         @RequestParam(required = false) Long albumId,
                                                         @RequestParam(required = false) String keyword) {
        return ApiResponse.success(materialService.authorizedMaterials(SecurityUtils.currentUserId(), categoryId, albumId, keyword));
    }

    @GetMapping("/materials/{id}")
    public ApiResponse<MaterialResponse> detail(@PathVariable Long id) {
        return ApiResponse.success(materialService.detail(SecurityUtils.currentUserId(), id));
    }

    @GetMapping("/materials/{id}/preview")
    public ApiResponse<Map<String, String>> preview(@PathVariable Long id) {
        return ApiResponse.success(Map.of("url", materialService.previewUrl(SecurityUtils.currentUserId(), id)));
    }

    @PostMapping("/materials/{id}/access")
    public ApiResponse<Void> access(@PathVariable Long id) {
        learningProgressService.access(SecurityUtils.currentUserId(), id);
        return ApiResponse.successMessage("记录成功");
    }

    @PutMapping("/materials/{id}/progress")
    public ApiResponse<Void> updateProgress(@PathVariable Long id, @Valid @RequestBody LearningProgressRequest request) {
        learningProgressService.update(SecurityUtils.currentUserId(), id, request);
        return ApiResponse.successMessage("更新成功");
    }

    @GetMapping("/continue-learning")
    public ApiResponse<List<Map<String, Object>>> continueLearning() {
        return ApiResponse.success(learningProgressService.continueLearning(SecurityUtils.currentUserId()));
    }
}
