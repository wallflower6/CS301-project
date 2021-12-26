package com.como.service.processapprequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.como.service.processapprequests.aws.S3Service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ProcessService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private RequestService requestService;

    @Autowired
    private JSONService jsonService;

    @Autowired
    private Dummify dummify;

    // @Value("${rapi_url}")
    // String api_url;

    private String receiveAPIURL = "http://ec2-18-142-21-164.ap-southeast-1.compute.amazonaws.com:8080" + "/gateway";

    public void executePointRedemption(String itemToRedeem, JSONObject dataJson, double cost, String memberNumber) {
        ResponseEntity<String> cachedItems = fetchFromCache("items");

        if (cachedItems == null) {
            cachedItems = requestService.createHttpRequest(null, "https://c21sguat.memgate.com/api/vouchertype/search?status=Active&type=THIRDPARTY", HttpMethod.GET);

            s3Service.saveResponseEntity("items", cachedItems, "items.json");
        }

        List<JSONObject> itemsList = jsonService.convertStringToJSONArray(cachedItems.getBody());

        Map<String, Object> voucher = new HashMap<>();
        for (JSONObject item : itemsList) {
            if (item.get("VoucherID").toString().equals(itemToRedeem)) {
                voucher.put("code", item.get("VoucherCode").toString());
                voucher.put("referenceNumber", "referenceNumber");
                voucher.put("voucherText", item.get("VoucherName").toString());
                voucher.put("description", item.get("ShortDescription").toString());
                voucher.put("voucherSource", "voucherSource");
            }
        }

        List<JSONObject> vouchers = new ArrayList<>();
        vouchers.add(new JSONObject(voucher));

        Map<String, Object> redeemDetails = new HashMap<>();
        redeemDetails.put("vouchers", vouchers);
        dataJson.put("redeemDetails", redeemDetails);

        System.out.println("REDEEM REWARD POST");
        System.out.println(dataJson);

        // Send to endpoint POST /reward/redeem with dataJson
        // Create packet for RAPI
        Map<String, String> requestToForward = new HashMap<>();
        requestToForward.put("action", "POST");
        requestToForward.put("vendor", "Memberson");
        requestToForward.put("url", "https://c21sguat.memgate.com/MockRewardApi/reward/redeem");
        requestToForward.put("data", dataJson.toJSONString());
        ResponseEntity<?> response = requestService.createHttpRequest(requestToForward, receiveAPIURL, HttpMethod.POST);

        System.out.println("FIRST ENDPOINT RESULT:");
        System.out.println(response);

        // Send to endpoint redeem-point
        JSONObject redeemPointData = dummify.dummifyPointRedemption(memberNumber, (int)cost);
        // Create packet for RAPI
        Map<String, String> requestToForward_2 = new HashMap<>();
        requestToForward_2.put("action", "POST");
        requestToForward_2.put("vendor", "Memberson");
        requestToForward_2.put("url", "https://c21sguat.memgate.com/api/transaction/redeem-point");
        requestToForward_2.put("data", redeemPointData.toJSONString());
    
        try {

            ResponseEntity<String> response_point = requestService.createHttpRequest(requestToForward_2, receiveAPIURL, HttpMethod.POST);
            System.out.println("SECOND ENDPOINT RESULT:");
            System.out.println(response_point);

        } catch (HttpClientErrorException e) {
            System.out.println("Here dwells a 400 Bad Request because of an invalid profile credential but let us proceed as if all is well.");
            return;
        }
    }
    
    public double redeemItem_calculateCost(String itemToRedeem) {
        ResponseEntity<String> cachedItems = fetchFromCache("items");

        if (cachedItems == null) {
            cachedItems = requestService.createHttpRequest(null, "https://c21sguat.memgate.com/api/vouchertype/search?status=Active&type=THIRDPARTY", HttpMethod.GET);

            s3Service.saveResponseEntity("items", cachedItems, "items.json");
        }

        List<JSONObject> itemsList = jsonService.convertStringToJSONArray(cachedItems.getBody());
        
        return getItemsTotalPointCost(itemsList, itemToRedeem);
    }

    public double getItemsTotalPointCost(List<JSONObject> itemsList, String itemToRedeem) {
        double total = -1;
        for (JSONObject item : itemsList) {
            if (item.get("VoucherID").toString().equals(itemToRedeem)) {
                double cost = Double.parseDouble(item.get("CostValue").toString());
                total += cost;
            }
        }

        // Some error occurred
        if (total < 0) return Double.POSITIVE_INFINITY;
        return total + 1;
    }

    public ResponseEntity<String> fetchFromCache(String key) {
        return s3Service.checkCacheReturnsEntity(key);
    }
}
