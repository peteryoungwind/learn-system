package com.learnsystem.service;

import java.io.InputStream;

public interface FileStorageService {
    StoredFile upload(String objectKey, InputStream inputStream, long size, String contentType);

    InputStream open(String objectKey);

    boolean exists(String objectKey);

    record StoredFile(String objectKey, String url, long size) {
    }
}
