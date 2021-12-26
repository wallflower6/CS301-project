package com.como.service.receiveapprequests;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(maxAge = 3600)
@Service
public class RARListener {

    // @Value("${par_url}")
    // private String par_url;
    
    private String processAppURI = "http://ip-10-0-6-81.ap-southeast-1.compute.internal:8080" + "/gateway";
    
    @CrossOrigin("http://ip-10-0-6-81.ap-southeast-1.compute.internal:8080/app")
    public ResponseEntity<String> forwardToPAR(@RequestBody String request){

        System.out.println("Forwarding: " + request);
        
        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> forwardRequest = new HttpEntity<>(request);
        ResponseEntity<String> responseFromPAR = null;

        // Get response from ReceiveAPIRequests and return to ReceiveAppRequests
        try {
            responseFromPAR = restTemplate.postForEntity(processAppURI, forwardRequest, String.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        return responseFromPAR;
    }
}
