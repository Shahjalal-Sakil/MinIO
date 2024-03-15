package org.practice.minio.controller;

import io.minio.ObjectWriteResponse;
import org.practice.minio.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;

@RestController
@RequestMapping("/file")
public class FileManager {
    private final MinioService minioService;

    public FileManager(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("bucket") String bucket){
        Path path = Path.of(file.getOriginalFilename());
        return minioService.upload(path, file, file.getContentType(), bucket);
    }

    @GetMapping("/{bucket}/{fileName}/{etag}")
    public ResponseEntity<InputStreamResource> download(@PathVariable("bucket") String bucket,@PathVariable("fileName") String fileName,
                                                        @PathVariable("etag") String etag) {
        InputStream inputStream = minioService.download(bucket, fileName, etag);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(new InputStreamResource(inputStream), httpHeaders, HttpStatus.OK);
    }
}
