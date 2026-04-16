package com.learnsystem.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialResponse {
    private Long id;
    private String title;
    private String author;
    private String fileType;
    private String storageProvider;
    private String objectKey;
    private String originalFilename;
    private String mimeType;
    private String previewObjectKey;
    private String previewStatus;
    private Long categoryId;
    private Long albumId;
    private String subtitle;
    private String summary;
    private String coverUrl;
    private Long fileSize;
    private String tags;
    private Integer sortOrder;
    private String remark;
    private String publishStatus;
    private LocalDateTime publishTime;
    private LocalDateTime ingestTime;
}
