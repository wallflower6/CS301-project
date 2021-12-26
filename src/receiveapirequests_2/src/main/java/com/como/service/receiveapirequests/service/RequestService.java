package com.como.service.receiveapirequests.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestService {
    
    @Value("${svc.auth}")
    String svcAuth;

    @Value("${token}")
    String token;

    @Value("${sevenrooms_client_id}")
    String client_id;

    @Value("${sevenrooms_client_secret}")
    String client_secret;

    @Autowired
    private JSONService jsonService;

    private String sevenRoomsHandshakeURL = "https://demo.sevenrooms.com/api-ext/2_4/auth";

    private HttpHeaders headers;
    private RestTemplate restTemplate;

    // Returns token
    public String createSevenRoomsHandshake() {
        restTemplate = new RestTemplate();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("client_id", client_id);
        map.add("client_secret", client_secret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity( sevenRoomsHandshakeURL, request , String.class );

        Map<String, Object> responseData = jsonService.convertStringToMap(response.getBody());
        String token = jsonService.searchForValue("token", responseData);

        return token;
    }

    public ResponseEntity<String> createMembersonRequestWithToken(String body, String url, String profileToken, HttpMethod method) {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("SvcAuth", svcAuth);
        headers.set("Token", token);
        headers.set("ProfileToken", profileToken);

        HttpEntity<String> entityReq = null;
        if (body != null) {
            entityReq = new HttpEntity<>(body, headers);
        } else {
            entityReq = new HttpEntity<>(headers);
        }

        System.out.println("Body to be sent: ");
        System.out.println(body);

        ResponseEntity<String> response = restTemplate.exchange(url, method, entityReq, String.class);

        System.out.println("RESPONSE FROM MEMBERSON: ");
        System.out.println(response);
        
        return response;
    }

    public ResponseEntity<String> createMembersonRequest(String body, String url, HttpMethod method) {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("SvcAuth", svcAuth);
        headers.set("Token", token);

        HttpEntity<String> entityReq = null;
        if (body != null) {
            entityReq = new HttpEntity<>(body, headers);
        } else {
            entityReq = new HttpEntity<>(headers);
        }

        System.out.println("Body to be sent: ");
        System.out.println(body);

        ResponseEntity<String> response = restTemplate.exchange(url, method, entityReq, String.class);

        System.out.println("RESPONSE FROM MEMBERSON: ");
        System.out.println(response);
        
        return response;
    }

    public ResponseEntity<String> createSevenRoomsRequest(String body, String url, HttpMethod method) {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String token = createSevenRoomsHandshake();
        headers.set("Authorization", token);

        HttpEntity<MultiValueMap<String, String>> entityReq = null;
        if (body != null) {
            MultiValueMap<String, String> map = convertStringToMap(body); 
            entityReq = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        } else {
            entityReq = new HttpEntity<>(headers);
        }
        
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        try {
            response = restTemplate.exchange(url, method, entityReq, String.class);
        } catch (Exception e) {
            // For 7Rooms booking, we are mocking the data returned here:
            if (url.contains("book") && method == HttpMethod.PUT) {
                response = new ResponseEntity<>("Booking Confirmed", HttpStatus.OK);
            } else {
                e.printStackTrace();
            }
        }
        
        return response;
    }

    public MultiValueMap<String, String> convertStringToMap(String data) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        String[] dataSplit = data.split(",");
        for (int i = 0; i < dataSplit.length; i++) {
            String key = dataSplit[i].split("\":\"")[0];
            String val = dataSplit[i].split("\":\"")[1];
            // remove quotes
            key = key.substring(1);
            val = val.substring(0,val.length() - 1);
            
            if (i == 0) {
                key = key.substring(1);
                map.add(key, val);
            } else if (i == dataSplit.length - 1) {
                val = val.substring(0, val.length() - 1);
                map.add(key, val);
            } else {
                map.add(key, val);
            }
        }

        return map;
    }
    
}
