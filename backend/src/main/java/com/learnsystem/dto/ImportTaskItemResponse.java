package com.learnsystem.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImportTaskItemResponse {
    private Long id;
    private Long taskId;
    private String originalFilename;
    private String fileType;
    private Long fileSize;
    private String storagePath;
    private String status;
    private String errorMessage;
    private Long materialId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
