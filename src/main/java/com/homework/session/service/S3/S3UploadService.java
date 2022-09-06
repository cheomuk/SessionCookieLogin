package com.homework.session.service.S3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class S3UploadService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String uploadFile(MultipartFile multipartFile) {
        //getFileExtension(multipartFile.getName());
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket, s3FileName, inputStream, objMeta)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new UnAuthorizedException("FILE_UPLOAD_FAILED", ACCESS_DENIED_EXCEPTION);
        }

        return s3Client.getUrl(bucket, s3FileName).toString();
    }

    // 파일 유효성 검사
    @Transactional
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new UnAuthorizedException("FILE_UPLOAD_ERROR", ACCESS_DENIED_EXCEPTION);
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new UnAuthorizedException("FILE_UPLOAD_ERROR", ACCESS_DENIED_EXCEPTION);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    // 업로드된 파일 Url 가져오기
    @Transactional
    public String getFileUrl(String fileName) {
        return s3Client.getUrl(bucket, fileName).toString();
    }

    // DeleteObject를 통해 S3 파일 삭제
    @Transactional
    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        s3Client.deleteObject(deleteObjectRequest);
    }
}
