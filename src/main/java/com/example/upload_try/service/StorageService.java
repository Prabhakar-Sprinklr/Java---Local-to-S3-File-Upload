package com.example.upload_try.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j

public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file){
        File fileObj=convertMultiPartFileToFile(file);
        String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        return "File uploaded : "+fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile=new File(file.getOriginalFilename());
        try(FileOutputStream fos=new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        }
        catch(IOException e){
            log.error("Error converting multipart file to file",e);
        }
        return convertedFile;
    }
}
