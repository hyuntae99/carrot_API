package com.hyunn.carrot.board;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AWSService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public AWSService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String getFileUrl(String bucketName, String fileKey) {
        return amazonS3.getUrl(bucketName, fileKey).toString();
    }

    public String getFileUrl(String fileKey) {
        return getFileUrl(bucketName, fileKey);
    }
}
