package com.como.service.receiveapprequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import io.micrometer.core.ipc.http.HttpSender.Response;

@Service
public class MemberService {

    @Autowired
    private RARListener listener;

    @Autowired
    private JSONService jsonService;

    @Autowired
    private Dummify dummify;

    public ResponseEntity<String> createNewUser(JSONObject newUser) {
        // Create profile on Memberson
        ResponseEntity<String> newProfile = createMember_Memberson(dummify.dummifyCreateMember(newUser.get("title").toString(), newUser.get("firstName").toString(), newUser.get("lastName").toString(), newUser.get("mobileCountryCode").toString(), newUser.get("mobile").toString(), newUser.get("email").toString(), newUser.get("username").toString()));

        if (newProfile == null) {
            System.out.println("ERROR REGISTERING ON MEMBERSON");
            return newProfile;
        }

        JSONObject newProfileJSO = jsonService.convertStringToJSONObj(newProfile.getBody());
        String customerNo = extractValueFromProfile(newProfileJSO, "CustomerNumber");
        String profileToken = extractValueFromProfile(newProfileJSO, "Token");

        // Set Password
        ResponseEntity<String> setPwd = setPassword_Memberson(newUser.get("password").toString(), customerNo, profileToken);
        if (setPwd.getStatusCode() != HttpStatus.OK) {
            System.err.println("UNABLE TO SET PASSWORD");
            return setPwd;
        }

        // Create profile on 7Rooms
        ResponseEntity<String> newClient = createMember_7Rooms(newUser.get("email").toString(), newUser.get("firstName").toString(), newUser.get("lastName").toString());

        if (newClient.getStatusCode() != HttpStatus.OK) {
            System.err.println("ERROR REGISTERING ON 7ROOMS");
            return newClient;
        }

        return new ResponseEntity<String>("Member created!", HttpStatus.OK);
    }

    public ResponseEntity<String> setPassword_Memberson(String password, String customerNo, String profileToken) {
        String uriToForward = "/api/profile/" + customerNo + "/" + password + "/webportal/set";

        // profileToken to be set in header
        Map<String, Object> dataToForward = new HashMap<>();
        dataToForward.put("Password", password);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("action", "POST");
        jsonMap.put("identifier", "setPassword_Memberson");
        jsonMap.put("profileToken", profileToken);
        jsonMap.put("uri", uriToForward);
        jsonMap.put("data", dataToForward);
        JSONObject jsonToForward = new JSONObject(jsonMap);

        System.out.println("RAR forwarding data to PAR: " + jsonToForward.toString());

        ResponseEntity<String> response = listener.forwardToPAR(jsonToForward.toString());

        return response;
    }

    public ResponseEntity<String> createMember_Memberson(JSONObject dataToForward) {

        if (dataToForward == null) return null;

        String uriToForward = "/api/profile/create-with-membership";

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("action", "POST");
        jsonMap.put("identifier", "createMember_Memberson");
        jsonMap.put("uri", uriToForward);
        jsonMap.put("data", dataToForward);
        JSONObject jsonToForward = new JSONObject(jsonMap);

        System.out.println("RAR forwarding data to PAR: " + jsonToForward.toString());

        ResponseEntity<String> response = listener.forwardToPAR(jsonToForward.toString());
        return response;

    }

    public List<JSONObject> checkMemberExist_Memberson(String email) {
        String uriToForward = "/api/profile/search-simple";
        Map<String, String> dataToForward = new HashMap<>();
        dataToForward.put("EmailAddress", email);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("action", "POST");
        jsonMap.put("identifier", "checkMemberExist_Memberson");
        jsonMap.put("uri", uriToForward);
        jsonMap.put("data", dataToForward);
        JSONObject jsonToForward = new JSONObject(jsonMap);

        System.out.println("RAR forwarding data to PAR: " + jsonToForward.toString());

        ResponseEntity<String> response = listener.forwardToPAR(jsonToForward.toString());

        if (response == null) return null;
        List<JSONObject> responseList = jsonService.convertStringToJSONArray(response.getBody());

        if (responseList == null) return null;
        return responseList;
    }

    public String getCustomerNumber_Memberson(List<JSONObject> memberDetails) {
        if (memberDetails == null) return null;
        if (memberDetails.get(0) == null) return null;
        return memberDetails.get(0).get("CustomerNumber").toString();
    }

    public String extractValueFromProfile(JSONObject profile, String param) {
        if (profile == null || param == null) return null;
        JSONObject profileObj = jsonService.convertStringToJSONObj(profile.get("Profile").toString());
        if (profileObj == null) return null;
        if (!profileObj.containsKey(param)) return null;
        return profileObj.get(param).toString();
    }

    public ResponseEntity<String> createMember_7Rooms(String email, String first_name, String last_name) {
        String uriToForward = "/clients";
        Map<String, String> dataToForward = new HashMap<>();
        dataToForward.put("email", email);
        dataToForward.put("first_name", first_name);
        dataToForward.put("last_name", last_name);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("action", "POST");
        jsonMap.put("identifier", "createMember_7Rooms");
        jsonMap.put("uri", uriToForward);
        jsonMap.put("data", dataToForward);
        JSONObject jsonToForward = new JSONObject(jsonMap);
        
        System.out.println("RAR forwarding data to PAR: " + jsonToForward.toString());

        ResponseEntity<String> response = listener.forwardToPAR(jsonToForward.toString());
        if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            System.out.println("Client exists!");
        }
    
        return response;
    }

    

}
