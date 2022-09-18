package com.tm.gogo.helper;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(String dirName, MultipartFile file) throws IOException {
        String fileName = createFileName(dirName, file);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), getObjectMetadata(file))
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(file.getContentType());
        return objectMetaData;
    }

    private String createFileName(String dirName, MultipartFile file) {
        String[] split = Objects.requireNonNull(file.getContentType()).split("/");
        String extension = split[split.length - 1];
        return dirName + "/" + UUID.randomUUID() + "." + extension;
    }
}
