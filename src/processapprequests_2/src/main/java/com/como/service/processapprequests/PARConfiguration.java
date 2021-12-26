package com.como.service.processapprequests;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PARConfiguration {

    @Bean
    public Map<String, String> linkMap() {
        Map<String, String> linkMap = new HashMap<>();

        // key: method name, value: api endpoint
        linkMap.put("getItemListing", "Memberson");
        linkMap.put("getVenueListing", "7Rooms");
        linkMap.put("bookVenue", "7Rooms");
        linkMap.put("createMember_7Rooms", "7Rooms");
        linkMap.put("checkMemberExist_Memberson", "Memberson");
        linkMap.put("createMember_Memberson", "Memberson");
        linkMap.put("redeemItem", "MembersonMock");
        linkMap.put("checkEmail", "Memberson");
        linkMap.put("setPassword_Memberson", "Memberson");
        return linkMap;
    }

    @Bean
    public Map<String, String> cacheKeyMap() {
        Map<String, String> cacheKeyMap = new HashMap<>();

        cacheKeyMap.put("getVenueListing", "venues");
        cacheKeyMap.put("getItemListing", "items");
        return cacheKeyMap;
    }

    @Bean
    public Map<String, String> vendorEndpoints() {
        Map<String, String> vendorEndpoints = new HashMap<>();

        // key: method name, value: api endpoint
        vendorEndpoints.put("7Rooms", "https://demo.sevenrooms.com/api-ext/2_4");
        vendorEndpoints.put("Memberson", "https://c21sguat.memgate.com");
        vendorEndpoints.put("MembersonMock", "https://c21sguat.memgate.com/MockRewardApi");
        return vendorEndpoints;
    }

}
