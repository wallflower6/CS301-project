package com.como.service.receiveapirequests;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoService {

    @Autowired
    private Map<String, String> GETResponses;
    
    public String GET(String url) {
        return GETResponses.get(url);
    }

    public void POST(String url, JSONObject body) {
        
    }

    public void PUT(String url, JSONObject body) {
        
    }

    public void DELETE(String url) {
        
    }
}
