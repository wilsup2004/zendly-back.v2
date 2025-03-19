package com.weight.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;
    private final String region;

    public S3Service(@Value("${aws.s3.access-key}") String accessKey,
                     @Value("${aws.s3.secret-key}") String secretKey,
                     @Value("${aws.s3.bucket.name}") String bucketName,
                     @Value("${aws.s3.region}") String region) {

        this.bucketName = bucketName;
        this.region = region;

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

 // Upload d'une image vers S3
    public String uploadFile(MultipartFile file, String directory) throws IOException {
    	String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String key = "images/" + directory + "/" +fileName;

        File convertedFile = convertMultiPartToFile(file);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(convertedFile));

        // Supprimer le fichier temporaire après l'upload
        convertedFile.delete();

        // return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
        
        return fileName;
    }
    
    
    // Télécharger une image depuis S3
    public  byte[]  downloadFile(String directory, String fileName) throws IOException {
        String key = "images/" + directory + "/" + fileName;
        //File downloadedFile = new File(fileName);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object =s3Client.getObject(getObjectRequest);

        return s3Object.readAllBytes();  // Convertit le fichier en byte[]
    }
    
    
    // Supprimer une image de S3
    public void deleteFile(String directory, String fileName) {
        String key = "images/" + directory + "/" + fileName;

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
    

    // Conversion MultipartFile en File temporaire
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    
}