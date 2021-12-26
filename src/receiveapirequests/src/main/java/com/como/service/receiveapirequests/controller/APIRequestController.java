package com.como.service.receiveapirequests.controller;

import java.util.Map;

import com.como.service.receiveapirequests.service.JSONService;
import com.como.service.receiveapirequests.service.RequestService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class APIRequestController {

    @Autowired
    private JSONService jsonService;

    @Autowired
    private RequestService requestService;

    // PAR 8080
    @CrossOrigin("http://ip-10-0-1-137.ap-southeast-1.compute.internal:8080")
    @RequestMapping(value="/gateway")
    public ResponseEntity<String> receiveForwardedRequests(@RequestBody Map<String, Object> requestJson) {
        System.out.println("Forwarded request received.");

        // Validate requestJson
        if (requestJson == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!requestJson.containsKey("action") || !requestJson.containsKey("url") || !requestJson.containsKey("vendor")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Extract action and uri
        String action = requestJson.get("action").toString();
        String url = requestJson.get("url").toString();
        String vendor = requestJson.get("vendor").toString();

        System.out.println("Sending a " + action + " to " + url + " for " + vendor);

        String data = null;
        if (requestJson.containsKey("data")) {
            data = requestJson.get("data").toString(); 
        }

        // Some considerations
        if (requestJson.containsKey("profileToken")) {
            
            return requestService.createMembersonRequestWithToken(data, url, requestJson.get("profileToken").toString(), HttpMethod.POST);
        }

        System.out.println("Querying vendor " + vendor + " at url " + url);

        if (action.equals("GET")) {
            try {
                return makeRequest(url, vendor, data, HttpMethod.GET);
            } catch(HttpClientErrorException e) {
                System.out.println(e.getMessage());
            }
        }
        
        if (action.equals("PUT")) {
            try {
                return makeRequest(url, vendor, data, HttpMethod.PUT);
            } catch(HttpClientErrorException e) {
                System.out.println(e.getMessage());
            }
        }
        
        if (action.equals("POST")) {
            try {
                return makeRequest(url, vendor, data, HttpMethod.POST);
            } catch(HttpClientErrorException e) {
                System.out.println(e.getMessage());
            }
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> makeRequest(String url, String vendor, String requestBody, HttpMethod method) {
        if (vendor.equals("Memberson")) {
            return requestService.createMembersonRequest(requestBody, url, method);
        }

        if (vendor.equals("7Rooms")) {
            return requestService.createSevenRoomsRequest(requestBody, url, method);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> GET(String url, String vendor, String requestBody) {
        if (vendor.equals("Memberson")) {
            return requestService.createMembersonRequest(requestBody, url, HttpMethod.GET);
        }

        if (vendor.equals("7Rooms")) {
            return requestService.createSevenRoomsRequest(requestBody, url, HttpMethod.GET);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> POST(String url, String vendor, String requestBody) {
        if (vendor.equals("Memberson")) {
            return requestService.createMembersonRequest(requestBody, url, HttpMethod.POST);
        }

        if (vendor.equals("7Rooms")) {
            return requestService.createSevenRoomsRequest(requestBody, url, HttpMethod.POST);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> PUT(String url, String vendor, String requestBody) {
        if (vendor.equals("Memberson")) {
            return requestService.createMembersonRequest(requestBody, url, HttpMethod.PUT);
        }

        if (vendor.equals("7Rooms")) {
            return requestService.createSevenRoomsRequest(requestBody, url, HttpMethod.PUT);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/aws-test")
    public ResponseEntity<String> awsIntegrationTest(@RequestBody String data) {
        
        JSONObject dataJson = jsonService.convertStringToJSONObj(data);
        String membersonEndpoint = dataJson.get("uri").toString();

        ResponseEntity<String> respEntity = makeRequest(membersonEndpoint, "Memberson", null, HttpMethod.GET);
        return respEntity;
    }

    @RequestMapping("/sevenrooms")
    public void testSevenRooms() {
        requestService.createSevenRoomsHandshake();
    }
}
