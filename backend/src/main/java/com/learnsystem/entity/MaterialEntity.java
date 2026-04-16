package com.learnsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("materials")
public class MaterialEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String author;
    private String fileType;
    private String storageProvider;
    private String objectKey;
    private String originalFilename;
    private String mimeType;
    private Long sourceImportTaskId;
    private String previewObjectKey;
    private String previewStatus;
    private Long categoryId;
    private Long albumId;
    private String subtitle;
    private String summary;
    private String coverUrl;
    private Long fileSize;
    private String tags;
    private Integer sortOrder;
    private String remark;
    private LocalDateTime ingestTime;
    private LocalDateTime publishTime;
    private String publishStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
