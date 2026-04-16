package com.learnsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaterialRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "作者不能为空")
    private String author;
    @NotBlank(message = "文件类型不能为空")
    private String fileType;
    private String objectKey;
    private String originalFilename;
    private String mimeType;
    private String previewObjectKey;
    private String previewStatus;
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    private Long albumId;
    private String subtitle;
    private String summary;
    private String coverUrl;
    private Long fileSize;
    private String tags;
    private Integer sortOrder;
    private String remark;
    @NotBlank(message = "发布状态不能为空")
    private String publishStatus;
}
