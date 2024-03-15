package org.practice.minio.service;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;

@Service
@Slf4j
public class MinioServiceImpl implements MinioService{
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioServiceImpl.class);
    private final MinioClient minioClient;

    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String upload(Path path, MultipartFile file, String contentType, String bucket) {

        try {
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("file/photo/" + file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .bucket(bucket)
                    .build();
            ObjectWriteResponse response =  minioClient.putObject(putObjectArgs);
            return response.etag();
        } catch (Exception e){
            LOGGER.error("Error occurred:{}", e);
            return null;
        }
    }

    @Override
    public InputStream download(String bucket, String fileName, String etag) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                            .object(fileName)
                    .matchETag(etag)
                    .build());
            return response;
        } catch (Exception e){
            LOGGER.error("Error occurred:{}", e);
            return null;
        }

    }
}
