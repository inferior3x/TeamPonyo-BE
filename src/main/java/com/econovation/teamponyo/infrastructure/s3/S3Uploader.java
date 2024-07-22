package com.econovation.teamponyo.infrastructure.s3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

//@Component
@ConfigurationProperties(prefix = "aws")
public class S3Uploader {
    private final S3Client s3Client;
    private final String bucketName;
    private final Region region = Region.AP_NORTHEAST_2;

//    private MultipartFile multipartFile;

    public S3Uploader(String bucketName, String accessKey, String secretKey) {
        this.bucketName = bucketName;
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(region)
                .build();
    }

    public List<String> uploadFiles(List<MultipartFile> multipartFiles) {
        List<CompletableFuture<String>> futures = multipartFiles.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> uploadFile(file)))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    public String uploadFile(MultipartFile multipartFile){
//        this.multipartFile = multipartFile;
        File file;
        file = convertMultiPartToFile(multipartFile);
        String keyName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
        return keyName;
    }

    public String getPublicUrl(String keyName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region.id(), keyName);
    }

    public List<String> getPublicUrls(List<String> keyNames){
        return keyNames.stream().map(this::getPublicUrl).toList();
    }

    public File downloadFile(String keyName){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        ResponseBytes<GetObjectResponse> s3ObjectBytes = s3Client.getObjectAsBytes(getObjectRequest);
        File downloadedFile = new File(System.getProperty("java.io.tmpdir") + "/" + keyName);
        try (FileOutputStream fos = new FileOutputStream(downloadedFile)) {
            fos.write(s3ObjectBytes.asByteArray());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return downloadedFile;
    }

    public List<File> downloadFiles(List<String> keyNames){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<File> downloadedFiles = keyNames.stream().map(this::downloadFile).toList();

        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return downloadedFiles;
    }

    private File convertMultiPartToFile(MultipartFile file){
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try {
            file.transferTo(convFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convFile;
    }

    public List<String> listFiles() {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);
        return listObjectsResponse.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
}