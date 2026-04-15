package com.learnsystem.service;

import com.learnsystem.dto.LearningProgressRequest;
import java.util.List;
import java.util.Map;

public interface LearningProgressService {
    void access(Long userId, Long materialId);

    void update(Long userId, Long materialId, LearningProgressRequest request);

    List<Map<String, Object>> continueLearning(Long userId);
}
