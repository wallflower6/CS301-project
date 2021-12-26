package com.como.service.receiveapirequests;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepoConfig {
    
    @Bean
    public Map<String, String> GETResponses() {
        Map<String, String> GETResponses = new HashMap<>();
        String[] urls = {
            "https://theapi/items",
            "https://theapi/member/1"
        };

        String[] bodies = {
            "{'item1':'gold', 'item2':'silver', 'item3':'bronze'}",
            "{'name':'john', 'email':'john@test.com', 'age':'20'}"
        };

        for (int i = 0; i < urls.length; i++) {
            GETResponses.put(urls[i], bodies[i]);
        }
        
        return GETResponses;
    }
}
