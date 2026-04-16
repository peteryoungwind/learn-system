package com.learnsystem.service;

import com.learnsystem.dto.StorageConfigRequest;
import com.learnsystem.dto.StorageConfigResponse;

public interface StorageConfigService {
    StorageConfigResponse get();

    StorageConfigResponse update(StorageConfigRequest request);
}
