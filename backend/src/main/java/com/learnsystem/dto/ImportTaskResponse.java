package com.learnsystem.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImportTaskResponse {
    private Long id;
    private String importType;
    private String status;
    private Integer totalCount;
    private Integer successCount;
    private Integer failCount;
    private String errorSummary;
    private String sourceProvider;
    private String sourceBucket;
    private String sourcePrefix;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
