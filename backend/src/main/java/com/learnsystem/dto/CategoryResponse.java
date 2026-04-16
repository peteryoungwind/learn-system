package com.learnsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String coverUrl;
    private Integer sortOrder;
    private String status;
}
