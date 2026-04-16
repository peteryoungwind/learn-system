package com.learnsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageConfigResponse {
    private Long id;
    private String provider;
    private String endpoint;
    private String bucket;
    private String region;
    private String accessKeyId;
    private String accessKeySecret;
    private String domain;
    private String basePath;
    private String status;
    private Boolean isDefault;
}
