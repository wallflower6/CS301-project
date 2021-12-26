package com.como.service.processapprequests;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PARService {

    @Autowired
    private Map<String, String> linkMap;

    @Autowired
    private Map<String, String> cacheKeyMap;

    @Autowired
    private Map<String, String> vendorEndpoints;

    public boolean validateInput(Map<String, Object> input) {
        if (input.containsKey("action") && input.containsKey("identifier")) return true;
        return false;
    }

    public String getCacheKey(String id) {
        return cacheKeyMap.get(id);
    }

    public String getMainVendor(String id) {
        String vendor = getVendor(id);
        if (vendor.equals("7Rooms")) return "7Rooms";
        if (vendor.contains("Memberson")) return "Memberson";
        return null;
    }

    public String getVendor(String id) {
        return linkMap.get(id);
    }

    public String getVendorEndpoint(String id) {
        String vendor = getVendor(id);
        if (vendor == null) return null;
        return vendorEndpoints.get(vendor);
    }

    public String getVendorEndpointByVendor(String vendor) {
        return vendorEndpoints.get(vendor);
    }
}
