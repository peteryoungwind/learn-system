package com.learnsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StorageConfigRequest {
    @NotBlank(message = "存储商不能为空")
    private String provider;
    @NotBlank(message = "endpoint不能为空")
    private String endpoint;
    @NotBlank(message = "bucket不能为空")
    private String bucket;
    private String region;
    @NotBlank(message = "AccessKeyId不能为空")
    private String accessKeyId;
    @NotBlank(message = "AccessKeySecret不能为空")
    private String accessKeySecret;
    private String domain;
    private String basePath;
    @NotBlank(message = "状态不能为空")
    private String status;
}
