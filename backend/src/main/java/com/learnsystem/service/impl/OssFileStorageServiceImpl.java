package com.learnsystem.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.learnsystem.entity.StorageConfigEntity;
import com.learnsystem.exception.BusinessException;
import com.learnsystem.service.FileStorageService;
import java.io.InputStream;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OssFileStorageServiceImpl implements FileStorageService {
    private final StorageConfigServiceImpl storageConfigService;

    public OssFileStorageServiceImpl(StorageConfigServiceImpl storageConfigService) {
        this.storageConfigService = storageConfigService;
    }

    @Override
    public StoredFile upload(String objectKey, InputStream inputStream, long size, String contentType) {
        StorageConfigEntity config = storageConfigService.getRequiredDefaultEntity();
        OSS client = buildClient(config);
        try {
            client.putObject(config.getBucket(), objectKey, inputStream);
            return new StoredFile(objectKey, resolveUrl(config, objectKey), size);
        } catch (Exception ex) {
            throw new BusinessException("上传到OSS失败: " + ex.getMessage());
        } finally {
            client.shutdown();
        }
    }

    @Override
    public InputStream open(String objectKey) {
        StorageConfigEntity config = storageConfigService.getRequiredDefaultEntity();
        OSS client = buildClient(config);
        try {
            OSSObject object = client.getObject(config.getBucket(), objectKey);
            return new ManagedOssInputStream(object.getObjectContent(), client);
        } catch (Exception ex) {
            client.shutdown();
            throw new BusinessException("读取OSS文件失败: " + ex.getMessage());
        }
    }

    @Override
    public boolean exists(String objectKey) {
        StorageConfigEntity config = storageConfigService.getRequiredDefaultEntity();
        OSS client = buildClient(config);
        try {
            return client.doesObjectExist(config.getBucket(), objectKey);
        } finally {
            client.shutdown();
        }
    }

    public String resolvePublicUrl(String objectKey) {
        StorageConfigEntity config = storageConfigService.getRequiredDefaultEntity();
        return resolveUrl(config, objectKey);
    }

    private OSS buildClient(StorageConfigEntity config) {
        return new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
    }

    private String resolveUrl(StorageConfigEntity config, String objectKey) {
        if (StringUtils.hasText(config.getDomain())) {
            String base = config.getDomain().replaceAll("/$", "");
            return base + "/" + objectKey;
        }
        String endpoint = config.getEndpoint().replaceFirst("^https?://", "");
        return "https://" + config.getBucket() + "." + endpoint + "/" + objectKey;
    }

    private static final class ManagedOssInputStream extends java.io.FilterInputStream {
        private final OSS client;

        private ManagedOssInputStream(InputStream in, OSS client) {
            super(in);
            this.client = client;
        }

        @Override
        public void close() throws java.io.IOException {
            try {
                super.close();
            } finally {
                client.shutdown();
            }
        }
    }
}
