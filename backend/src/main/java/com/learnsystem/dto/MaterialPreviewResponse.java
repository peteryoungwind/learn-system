package com.learnsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialPreviewResponse {
    private String viewerType;
    private String previewUrl;
    private String downloadUrl;
    private Boolean onlineSupported;
    private String fallbackMessage;
    private String previewStatus;
}
