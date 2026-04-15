package com.learnsystem.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class UpdateUserPermissionsRequest {
    @NotNull(message = "分类权限不能为空")
    private List<Long> categoryIds;
}
