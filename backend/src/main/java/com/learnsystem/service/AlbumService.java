package com.learnsystem.service;

import com.learnsystem.dto.AlbumRequest;
import com.learnsystem.dto.AlbumResponse;
import java.util.List;

public interface AlbumService {
    List<AlbumResponse> listAll();

    List<AlbumResponse> listAuthorized(Long userId, Long categoryId);

    List<AlbumResponse> listUserVisible(Long userId, Long categoryId);

    AlbumResponse create(AlbumRequest request);

    AlbumResponse update(Long id, AlbumRequest request);

    void delete(Long id);
}
