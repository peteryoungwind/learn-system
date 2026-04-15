package com.learnsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "分类名称不能为空")
    private String name;
    @NotBlank(message = "分类编码不能为空")
    private String code;
    private String description;
    private Integer sortOrder;
    @NotBlank(message = "状态不能为空")
    private String status;
}
