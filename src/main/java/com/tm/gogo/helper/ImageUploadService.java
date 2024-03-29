package com.tm.gogo.helper;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tm.gogo.web.image.ImageDirectory;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageUploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadAll(ImageDirectory dirName, List<MultipartFile> files) {
        return files.stream().map(file -> upload(dirName, file)).collect(Collectors.toList());
    }

    public String upload(ImageDirectory dirName, MultipartFile file) {
        String fileName = createFileName(dirName, file);
        PutObjectRequest putObjectRequest = getPutObjectRequest(file, fileName);
        amazonS3Client.putObject(putObjectRequest);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private PutObjectRequest getPutObjectRequest(MultipartFile file, String fileName) {
        try {
            return new PutObjectRequest(bucket, fileName, file.getInputStream(), getObjectMetadata(file))
                    .withCannedAcl(CannedAccessControlList.PublicRead);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다 file: " + fileName);
        }
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(file.getContentType());
        return objectMetaData;
    }

    private String createFileName(ImageDirectory dirName, MultipartFile file) {
        String[] split = Objects.requireNonNull(file.getContentType()).split("/");
        String extension = split[split.length - 1];
        return dirName + "/" + UUID.randomUUID() + "." + extension;
    }

    public void deleteAll(ImageDirectory dirName, List<String> imageNames) {
        imageNames.forEach(name -> delete(dirName, name));
    }

    public void delete(ImageDirectory dirName, String imageName) {
        String fileName = dirName + "/" + imageName;
        amazonS3Client.deleteObject(bucket, fileName);
    }
}
