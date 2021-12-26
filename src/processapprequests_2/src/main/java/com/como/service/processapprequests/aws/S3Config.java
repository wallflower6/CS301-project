package com.como.service.processapprequests.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;

@Configuration
@PropertySource("classpath:aws.properties")
public class S3Config {
    // @Value( "${AWS_ACCESS_KEY_ID}" )
    // private String AWS_ACCESS_KEY_ID;

    // @Value( "${AWS_SECRET_ACCESS_KEY}" )
    // private String AWS_SECRET_ACCESS_KEY;

    @Autowired
    private Environment env;

    // @Bean
    // public AmazonS3 getAmazonS3Client() {
    //     final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, accessSecret);
    //     // Get Amazon S3 client and return the S3 client object
    //     return AmazonS3ClientBuilder
    //             .standard()
    //             .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
    //             .withRegion(region)
    //             .build();
    // }

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials creds = new BasicAWSCredentials(env.getProperty("AWS_ACCESS_KEY_ID"), env.getProperty("AWS_SECRET_ACCESS_KEY"));
        return (AmazonS3Client) AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(creds)).disableChunkedEncoding().build();


        // return (AmazonS3Client) AmazonS3ClientBuilder.standard()
        
        //         .withCredentials(new DefaultAWSCredentialsProviderChain())
        //         .withRegion(Regions.AP_SOUTHEAST_1)
        //         .build();
    }
}
