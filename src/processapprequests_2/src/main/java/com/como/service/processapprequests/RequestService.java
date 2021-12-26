package com.como.service.processapprequests;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestService {

    @Value("${svc.auth}")
    String svcAuth;

    @Value("${token}")
    String token;

    private RestTemplate restTemplate;

    public ResponseEntity<String> createHttpRequest(Map<String, String> data, String url, HttpMethod method) {

        restTemplate = new RestTemplate();
        HttpEntity<Map<String, String>> request = new HttpEntity<>(data);
        ResponseEntity<String> response = null;

        // Get response from ReceiveAPIRequests and return to ReceiveAppRequests
        try {
            response = restTemplate.exchange(url, method, request, String.class);
        } catch (Exception e) {
            
        }

        return response;
    }

    public ResponseEntity<String> createHttpRequestObject(Map<String, Object> data, String url, HttpMethod method) {

        restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data);
        ResponseEntity<String> response = null;

        // Get response from ReceiveAPIRequests and return to ReceiveAppRequests
        try {
            response = restTemplate.exchange(url, method, request, String.class);
        } catch (Exception e) {
            
        }

        return response;
    }

    public ResponseEntity<String> createHttpRequestWithConsiderations(Map<String, Object> data, String url, HttpMethod method, List<String> extraParams) {

        data.put("extraParams", extraParams.toString());

        restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data);
        ResponseEntity<String> response = null;

        // Get response from ReceiveAPIRequests and return to ReceiveAppRequests
        try {
            response = restTemplate.exchange(url, method, request, String.class);
        } catch (Exception e) {
            
        }

        return response;
    }
}
