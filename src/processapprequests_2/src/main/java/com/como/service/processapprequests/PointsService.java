package com.como.service.processapprequests;

import java.util.HashMap;
import java.util.Map;

import com.como.service.processapprequests.aws.S3Service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class PointsService {
    @Autowired
    private S3Service s3Service;

    @Autowired
    private JSONService jsonService;

    @Autowired
    private PARService parService;

    @Autowired
    private RequestService reqService;

    private String receiveAPIURL = "http://ec2-18-142-21-164.ap-southeast-1.compute.amazonaws.com:8080" + "/gateway";

    public boolean comparePoints(double cost, JSONObject dataJson) {
        double balance = 0;

        if (!dataJson.containsKey("memberNumber")) return false;
        String memberNumber = dataJson.get("memberNumber").toString();
        // if (memberNumber == null) return false;
        System.out.println("Member number: " + memberNumber);
        String cacheKey = memberNumber + "-points";

        // Check cache
        if(s3Service.checkKeyExists(cacheKey)) {
            String points = s3Service.findData(cacheKey);
            balance = Double.parseDouble(points);
        } else {
            balance = getMemberBalance(memberNumber);
        }

        System.out.println("MEMBER BALANCE: ");
        System.out.println(balance);

        if (balance >= cost) return true;
        return false;
    }

    private double getMemberBalance(String memberNumber) {
        String endpoint = parService.getVendorEndpointByVendor("Memberson") + "/api/profile/" + memberNumber + "/summary";

        Map<String, String> requestToForward = new HashMap<>();
        requestToForward.put("action", "GET");
        requestToForward.put("url", endpoint);
        requestToForward.put("vendor", "Memberson");

        try {
            ResponseEntity<String> memberSummary = reqService.createHttpRequest(requestToForward, receiveAPIURL, HttpMethod.POST);
            System.out.println("Member summary: ");
            System.out.println(memberSummary);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
            // set to Double.POSITIVE_INFINITY to simulate sufficient balance
            // set to 0 to simulate insufficient balance
            return Double.POSITIVE_INFINITY; 
        }
        
        return Double.POSITIVE_INFINITY;
    }

    private ResponseEntity<String> redeemWithPoints(String memberNumber, int amount) {
        String endpoint = parService.getVendorEndpointByVendor("Memberson") + "/api/transaction/redeem-point";

        Map<String, Object> body = new HashMap<>();
        body.put("MemberNo", memberNumber);
        body.put("PointType", "C21 Points");
        body.put("TransactionDate", "2013-02-28T00:00:00+08:00");
        body.put("Location", "LHQS");
        body.put("Amount", amount);
        body.put("RedemptionCode", "RD001");
        body.put("Description", "Instant Redemption");
        body.put("ReceiptNo", "00234598");
        JSONObject json = new JSONObject(body);

        Map<String, String> requestToForward = new HashMap<>();
        requestToForward.put("action", "POST");
        requestToForward.put("url", endpoint);
        requestToForward.put("vendor", "Memberson");
        requestToForward.put("data", json.toJSONString());
        
        return reqService.createHttpRequest(requestToForward, receiveAPIURL, HttpMethod.POST);
    }

    private boolean processConfirmation(JSONObject reqConfirmed) {

        // Get member balance (cache or otherwise)
        // Get cost from reqConfirmed
        // Deduct cost from balance
        // Do redeem-point
        // If redeem-point successful: Store balance back in cache

        JSONObject dataJson = jsonService.convertStringToJSONObj(reqConfirmed.get("data").toString());
        if (!reqConfirmed.containsKey("cost")) return false;
        int cost = Integer.parseInt(reqConfirmed.get("cost").toString());

        if (!dataJson.containsKey("memberNumber")) return false;
        String memberNumber = dataJson.get("memberNumber").toString();
        if (memberNumber == null) return false;
        String cacheKey = memberNumber + "-points";

        // Check cache
        double balance = 0;
        if(s3Service.checkKeyExists(cacheKey)) {
            String points = s3Service.findData(cacheKey);
            balance = Double.parseDouble(points);
        } else {
            balance = getMemberBalance(memberNumber);
        }

        double newBalance = balance - cost;

        ResponseEntity<String> redeemPoint = redeemWithPoints(memberNumber, cost);
        if (redeemPoint.getStatusCode() == HttpStatus.OK) {
            s3Service.saveString(cacheKey, String.valueOf(newBalance));
            return true;
        }

        return false;

    }
}
