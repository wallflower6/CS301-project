package com.como.service.receiveapirequests.controller;


import com.como.service.receiveapirequests.RepoService;
import com.como.service.receiveapirequests.service.JSONService;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

@RestController
public class APIController {

    @Autowired
    private JSONService jsonService;

    @Autowired
    private RepoService repo;
    
    String apiEndpoint = "https://theapi/";

    @RequestMapping(value="/gateway")
    public void receiveForwardedRequests(@RequestBody String request) {
        
        JSONObject requestJson = jsonService.convertStringToJSONObj(request);
        
        if (requestJson.get("action").equals("GET")) {
            String fullURI = apiEndpoint + requestJson.get("uri").toString();
            System.out.println(repo.GET(fullURI));
        }

        /*
        Types of Requests:
        - GET:
            - uri
            - action (GET)
        - POST:
            - uri
            - body with required params
            - action (POST)
        - PUT
        */
    }

}