package com.learnsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LearningProgressRequest {
    @NotBlank(message = "已读状态不能为空")
    private String readStatus;
    @NotBlank(message = "完成状态不能为空")
    private String completionStatus;
}
