package com.como.service.receiveapirequests.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JSONService {
    private JSONParser parser = new JSONParser();
    private ObjectMapper mapper = new ObjectMapper();
    
    public JSONObject convertStringToJSONObj(String str) {
        
        try {
            JSONObject strJson = (JSONObject) parser.parse(str);
            return strJson;
        } catch (ParseException e) {
            return null;
        }
        
    }

    public List<String> convertStringToList(String str) {
        List<String> list = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            //Convert Map to JSON
            list = Arrays.asList(mapper.readValue(str, String[].class));

        } catch (JsonGenerationException e) {
            System.out.println("Failed to parse JSON, JsonGenerationException");
        } catch (JsonMappingException e) {
            System.out.println("Failed to parse JSON, JsonMappingException");
        } catch (IOException e) {
            System.out.println("Failed to parse JSON, IOException");
        }
        
        return list;
    }

    public Map<String, Object> convertStringToMap(String str) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            //Convert Map to JSON
            map = mapper.readValue(str, new TypeReference<Map<String, Object>>(){});

        } catch (JsonGenerationException e) {
            System.out.println("Failed to parse JSON.");
        } catch (JsonMappingException e) {
            System.out.println("Failed to parse JSON.");
        } catch (IOException e) {
            System.out.println("Failed to parse JSON.");
        }
        
        return map;
    }

    @SuppressWarnings("unchecked")
    public String searchForValue(String key, Map<String, Object> json) {

        if (json.containsKey(key)) return json.get(key).toString();

        Map<String, Object> curr = null;
        for (String jsonKey : json.keySet()) {

            if (json.get(jsonKey).getClass().getSimpleName().equals("LinkedHashMap")) {
                Object nestedJson = json.get(jsonKey);
                curr = mapper.convertValue(nestedJson, Map.class);
                return searchForValue(key, curr);
            }
        }

        return "Key not found";
    }

    @SuppressWarnings("unchecked")
    public String searchForValueJSO(String key, JSONObject json) {
        
        if (json.containsKey(key)) return json.get(key).toString();

        String value = null;
        
        Set<String> keys = json.keySet();
        for(String jsonKey : keys){
            if ( json.get(jsonKey) instanceof JSONObject ) {
                value = searchForValueJSO(key, (JSONObject)json.get(jsonKey));
            }
        }

        return value;
    }
}
