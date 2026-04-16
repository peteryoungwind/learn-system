package com.learnsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportTaskCreateRequest {
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    @NotNull(message = "专辑不能为空")
    private Long albumId;
    private String taskName;
    private String publishStatus;
    private MultipartFile[] files;
}
