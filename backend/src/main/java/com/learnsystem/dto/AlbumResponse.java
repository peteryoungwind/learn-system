package com.learnsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlbumResponse {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private String coverUrl;
    private Integer sortOrder;
    private String status;
}
