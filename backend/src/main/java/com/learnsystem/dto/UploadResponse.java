package com.learnsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponse {
    private String url;
    private String objectKey;
    private Long fileSize;
    private String originalFilename;
    private String fileType;
    private String previewObjectKey;
    private String previewStatus;
}
