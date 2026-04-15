package com.learnsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlbumRequest {
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    @NotBlank(message = "专辑名称不能为空")
    private String name;
    private String description;
    private Integer sortOrder;
    @NotBlank(message = "状态不能为空")
    private String status;
}
