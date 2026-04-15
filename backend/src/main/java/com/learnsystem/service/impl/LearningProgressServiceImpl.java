package com.learnsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsystem.dto.LearningProgressRequest;
import com.learnsystem.entity.LearningProgressEntity;
import com.learnsystem.entity.MaterialEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.mapper.LearningProgressMapper;
import com.learnsystem.mapper.MaterialMapper;
import com.learnsystem.service.LearningProgressService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LearningProgressServiceImpl implements LearningProgressService {
    private final LearningProgressMapper learningProgressMapper;
    private final MaterialMapper materialMapper;

    public LearningProgressServiceImpl(LearningProgressMapper learningProgressMapper, MaterialMapper materialMapper) {
        this.learningProgressMapper = learningProgressMapper;
        this.materialMapper = materialMapper;
    }

    @Override
    public void access(Long userId, Long materialId) {
        MaterialEntity material = materialMapper.selectById(materialId);
        if (material == null) {
            throw new BusinessException("资料不存在");
        }
        LearningProgressEntity progress = learningProgressMapper.selectOne(new LambdaQueryWrapper<LearningProgressEntity>()
                .eq(LearningProgressEntity::getUserId, userId)
                .eq(LearningProgressEntity::getMaterialId, materialId));
        LocalDateTime now = LocalDateTime.now();
        if (progress == null) {
            progress = new LearningProgressEntity();
            progress.setUserId(userId);
            progress.setMaterialId(materialId);
            progress.setReadStatus("UNREAD");
            progress.setCompletionStatus("IN_PROGRESS");
            progress.setFirstAccessedAt(now);
            progress.setLastAccessedAt(now);
            learningProgressMapper.insert(progress);
            return;
        }
        progress.setLastAccessedAt(now);
        learningProgressMapper.updateById(progress);
    }

    @Override
    public void update(Long userId, Long materialId, LearningProgressRequest request) {
        LearningProgressEntity progress = learningProgressMapper.selectOne(new LambdaQueryWrapper<LearningProgressEntity>()
                .eq(LearningProgressEntity::getUserId, userId)
                .eq(LearningProgressEntity::getMaterialId, materialId));
        if (progress == null) {
            access(userId, materialId);
            progress = learningProgressMapper.selectOne(new LambdaQueryWrapper<LearningProgressEntity>()
                    .eq(LearningProgressEntity::getUserId, userId)
                    .eq(LearningProgressEntity::getMaterialId, materialId));
        }
        progress.setReadStatus(request.getReadStatus());
        progress.setCompletionStatus(request.getCompletionStatus());
        progress.setLastAccessedAt(LocalDateTime.now());
        learningProgressMapper.updateById(progress);
    }

    @Override
    public List<Map<String, Object>> continueLearning(Long userId) {
        return learningProgressMapper.selectList(new LambdaQueryWrapper<LearningProgressEntity>()
                        .eq(LearningProgressEntity::getUserId, userId)
                        .ne(LearningProgressEntity::getCompletionStatus, "COMPLETED")
                        .orderByDesc(LearningProgressEntity::getLastAccessedAt)
                        .last("limit 10"))
                .stream()
                .map(progress -> Map.<String, Object>of(
                        "materialId", progress.getMaterialId(),
                        "readStatus", progress.getReadStatus(),
                        "completionStatus", progress.getCompletionStatus(),
                        "lastAccessedAt", progress.getLastAccessedAt()))
                .toList();
    }
}
