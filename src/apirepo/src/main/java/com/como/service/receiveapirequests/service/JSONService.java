package com.como.service.receiveapirequests.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class JSONService {

    private JSONParser parser = new JSONParser();
    
    public JSONObject convertStringToJSONObj(String str) {
        
        try {
            JSONObject strJson = (JSONObject) parser.parse(str);
            return strJson;
        } catch (ParseException e) {
            return null;
        }
        
    }
}
