package com.como.service.processapprequests;

import java.util.HashMap;
import java.util.Map;

import com.como.service.processapprequests.aws.S3Service;

// import com.como.service.processapprequests.service.S3Service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(maxAge = 3600)
@RestController
public class PARController {

    @Autowired
    private PARService parService;

    @Autowired
    private RequestService reqService;

    @Autowired
    private JSONService jsonService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProcessService process;

    @Autowired
    private PointsService pointsService;

    // @Value("${rapi_url}")
    // String api_url;


    private String receiveAPIURL = "http://ec2-18-142-21-164.ap-southeast-1.compute.amazonaws.com:8080" + "/gateway";


    // Consumes requests from ReceiveAppRequests (8081)
    @CrossOrigin("http://ec2-18-140-152-81.ap-southeast-1.compute.amazonaws.com:8080")
    @PostMapping(path="/gateway")
    public ResponseEntity<?> receiveRequests(@RequestBody String request) {

        // Format request string into json
        JSONObject requestJson = jsonService.convertStringToJSONObj(request);

        // Validate input
        if (requestJson == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!parService.validateInput(requestJson)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        
        // Get request parameters
        String id = requestJson.get("identifier").toString();
        String cacheKey = parService.getCacheKey(id);

        // Check cache
        ResponseEntity<String> cacheResult = checkCacheReturnsEntity(cacheKey);
        if (cacheResult != null) return cacheResult;

        String action = requestJson.get("action").toString();
        String uri = requestJson.get("uri").toString();
        String vendor = parService.getMainVendor(id);
        String vendorEndpoint = parService.getVendorEndpoint(id);

        // Ensure has been assigned to a vendor and has mapping
        if (vendorEndpoint == null || uri == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        
        String apiURL = vendorEndpoint + uri;

        // Corner case
        if (id.equals("setPassword_Memberson")) {

            // Prepare mapping to send to RApi
            Map<String, Object> requestToForward = new HashMap<>();

            // Check if there's a request body to be sent to API
            if (requestJson.containsKey("data")) {
                requestToForward.put("data", requestJson.get("data").toString());
            }

            requestToForward.put("action", action);
            requestToForward.put("vendor", vendor);
            requestToForward.put("url", apiURL);

            String profileToken = requestJson.get("profileToken").toString();
            requestToForward.put("profileToken", profileToken);

            System.out.println("Forwarding: " + requestToForward.toString());

            return reqService.createHttpRequestObject(requestToForward, receiveAPIURL, HttpMethod.POST);
        }

        // Prepare mapping to send to RApi
        Map<String, Object> requestToForward = new HashMap<>();

        // Check if there's a request body to be sent to API
        if (requestJson.containsKey("data")) {
            System.out.println("Data to send to API:");
            System.out.println(requestJson.get("data"));
            requestToForward.put("data", requestJson.get("data").toString());
        }

        requestToForward.put("action", action);
        requestToForward.put("vendor", vendor);
        requestToForward.put("url", apiURL);
        
        // Corner case 2
        if (id.equals("redeemItem")) {
            System.out.println("REDEEMING ITEM ON PAR");
            JSONObject dataJson = jsonService.convertStringToJSONObj(requestJson.get("data").toString());
            String memberNumber = requestJson.get("memberNumber").toString();
            // List<String> itemsToRedeem = jsonService.convertStringToList(requestJson.get("itemsToRedeem").toString());
            String itemToRedeem = requestJson.get("itemToRedeem").toString();

            double cost = process.redeemItem_calculateCost(itemToRedeem);
            System.out.println("COST:");
            System.out.println(cost);
            if (!(pointsService.comparePoints(cost, dataJson))) {
                System.out.println("INSUFFICIENT BALANCE");
                return new ResponseEntity<String>("Insufficient points.", HttpStatus.OK);
            }

            process.executePointRedemption(itemToRedeem, dataJson, cost, memberNumber);
            return new ResponseEntity<String>("Transaction succeeded.", HttpStatus.OK);
        }

        

        System.out.println("Forwarding: " + requestToForward.toString());
        ResponseEntity<String> response = reqService.createHttpRequestObject(requestToForward, receiveAPIURL, HttpMethod.POST);

        if (response != null && cacheKey != null) {
            storeInCache(cacheKey, response);
            return response;
        }

        if (response != null) {
            return response;
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Move cache functions into own service

    // Store in cache as file <key>.json
    public void storeInCache(String key, ResponseEntity<?> data) {
        // JSONObject dataJson = new JSONObject(data);
        // s3Service.saveJsonFile("key", data.getBody(), key + ".json");
        System.out.println("Calling storeInCache...");
        s3Service.saveResponseEntity(key, data, key + ".json");
    }

    // Used in / moved to S3Service
    // Check if a key is in the cache - returns a ResponseEntity
    public ResponseEntity<String> checkCacheReturnsEntity(String key) {
        if (!s3Service.checkKeyExists(key)) return null;
        String value = s3Service.findData(key);
        if (value == null) return null;
        return new ResponseEntity<>(value,HttpStatus.OK);
    }
    
    // Check if a key is in the cache - returns a map
    public Map<String, Object> checkCache(String key) {
        if (!s3Service.checkKeyExists(key)) return null;
        String value = s3Service.findData(key);
        Map<String, Object> map = jsonService.convertStringToMap(value);
        System.out.println(map);
        return map;
    }

    @CrossOrigin("http://ec2-18-140-152-81.ap-southeast-1.compute.amazonaws.com:8080") // change to url of RApp instance
    @PostMapping("/aws-test")
    public ResponseEntity<String> awsIntegrationTest(@RequestBody String data) {
        String receiveApiUrl = "http://ec2-18-142-21-164.ap-southeast-1.compute.amazonaws.com:8080" + "/aws-test"; // change host to RApi

        Map<String, String> requestToForward = new HashMap<>();
        requestToForward.put("uri", "https://c21sguat.memgate.com/MockRewardApi/member/01820934/rewards?includeMedia=false&includeRewardPricing=false&includeLinkedItems=false&includeRedeemedItem=false");
        JSONObject forwardJson = new JSONObject(requestToForward);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<JSONObject> forwardRequest = new HttpEntity<>(forwardJson);
        ResponseEntity<String> responseFromRApi = null;

        try {
            responseFromRApi = restTemplate.postForEntity(receiveApiUrl, forwardRequest, String.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        return responseFromRApi;
    }

    // Quick health test
    @RequestMapping("/health")
    public String healthCheck() {
        return "{'message': 'Service is healthy.'}";
    }
}
