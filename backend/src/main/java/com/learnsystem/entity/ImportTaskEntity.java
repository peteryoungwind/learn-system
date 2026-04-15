package com.learnsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("import_tasks")
public class ImportTaskEntity {
    @TableId(type = IdType.AUTO)
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
