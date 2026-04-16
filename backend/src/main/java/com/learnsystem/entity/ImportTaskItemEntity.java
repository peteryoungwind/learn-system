package com.learnsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("import_task_items")
public class ImportTaskItemEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long taskId;
    private String originalFilename;
    private String fileType;
    private Long fileSize;
    private String storagePath;
    private String status;
    private String errorMessage;
    private Long materialId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
