package com.learnsystem.service;

import com.learnsystem.dto.ImportTaskCreateRequest;
import com.learnsystem.dto.ImportTaskItemResponse;
import com.learnsystem.dto.ImportTaskResponse;
import java.io.IOException;
import java.util.List;

public interface ImportTaskService {
    List<ImportTaskResponse> list();

    ImportTaskResponse create(Long userId, ImportTaskCreateRequest request) throws IOException;

    List<ImportTaskItemResponse> items(Long taskId);
}
