package org.practice.minio.service;

import io.minio.ObjectWriteResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;

public interface MinioService {
    String upload(Path path, MultipartFile file, String contentType, String bucket);
    InputStream download( String bucket,String fileName, String etag);

}
