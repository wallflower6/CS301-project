package com.como.service.processapprequests.aws;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class S3Service {
    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Autowired
    private AmazonS3Client s3Client;

    public S3Service(AmazonS3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void saveString(String key, String val) {
        s3Client.putObject(bucketName, key, val);
    }

    public void saveJsonFile(String key, JSONObject jsonObject, String filename) {
        try {
            File file = new File(filename);
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.close();

            s3Client.putObject(bucketName, key, file);
        } catch (IOException e) {
            System.out.println("Error saving json file.");
        }
    }

    public void saveResponseEntity(String key, ResponseEntity<?> resEntity, String filename) {
        try {
            File file = new File(filename);
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(resEntity.getBody().toString());
            fileWriter.close();

            System.out.println("Storing in cache: ");
            System.out.println(resEntity.toString());

            s3Client.putObject(bucketName, key, file);
        } catch (IOException e) {
            System.out.println("Error saving ResEntity file.");
        }
    }

    public List<String> getAllStoredKeys() {
        List<String> keys = new ArrayList<>();
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            keys.add(os.getKey());
        }

        return keys;
    }

    public boolean checkKeyExists(String key) {
        return getAllStoredKeys().contains(key);
    }

    public String findData(String key) {
        S3Object s3object = s3Client.getObject(bucketName, key);
        if (s3object == null) return null;
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        // FileUtils.copyInputStreamToFile(inputStream, new File("/Users/user/Desktop/hello.txt"));

        String value = inputStreamToString(inputStream);
        return value;
    }

    // Check if a key is in the cache - returns a ResponseEntity
    public ResponseEntity<String> checkCacheReturnsEntity(String key) {
        if (!checkKeyExists(key)) return null;
        String value = findData(key);
        if (value == null) return null;
        return new ResponseEntity<>(value,HttpStatus.OK);
    }

    private String inputStreamToString(S3ObjectInputStream inputStream) {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
        (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            return null;
        }
        return textBuilder.toString();
    }
}
