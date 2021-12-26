package com.como.service.processapprequests.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class S3Properties {
    // Unused
    public void setProperty() {
        System.setProperty("aws.accessKeyId", "AKIA2FRH44W3DO4H6TF4");
        System.setProperty("aws.secretKey", "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");
    }
}
