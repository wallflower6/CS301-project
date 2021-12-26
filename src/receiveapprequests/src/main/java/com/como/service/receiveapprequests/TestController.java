package com.como.service.receiveapprequests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    // @Value("${par_url}")
    // String par_url;

    @Autowired
    private JSONService jsonService;
    
    @GetMapping("/aws-test")
    public ResponseEntity<String> awsIntegrationTest() {

        String processAppRequestURL = "http://ip-10-0-1-137.ap-southeast-1.compute.internal:8080" + "/aws-test";
        

        HttpEntity<String> entityReq = new HttpEntity<>("{'some':'stuff'}");
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> responseFromPAR = null;

        try {
            responseFromPAR = template.postForEntity(processAppRequestURL, entityReq, String.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        return responseFromPAR;
    }
}
