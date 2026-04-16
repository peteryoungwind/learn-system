package com.learnsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileTypePresetResponse {
    private Long id;
    private String code;
    private String name;
    private String extensions;
    private String mimeTypes;
    private String previewMode;
    private Boolean enabled;
    private Integer sortOrder;
}
