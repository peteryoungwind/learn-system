package com.learnsystem.controller;

import com.learnsystem.common.ApiResponse;
import com.learnsystem.dto.AlbumRequest;
import com.learnsystem.dto.AlbumResponse;
import com.learnsystem.service.AlbumService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/albums")
@PreAuthorize("hasRole('ADMIN')")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ApiResponse<List<AlbumResponse>> list() {
        return ApiResponse.success(albumService.listAll());
    }

    @PostMapping
    public ApiResponse<AlbumResponse> create(@Valid @RequestBody AlbumRequest request) {
        return ApiResponse.success(albumService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<AlbumResponse> update(@PathVariable Long id, @Valid @RequestBody AlbumRequest request) {
        return ApiResponse.success(albumService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        albumService.delete(id);
        return ApiResponse.successMessage("删除成功");
    }
}
