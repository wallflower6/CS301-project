package com.como.service.receiveapprequests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VenueController {
    @Autowired
    private VenueService venueService = new VenueService();

    @Autowired
    private RARListener listener;

    @Autowired
    private JSONService jsonService;

    @SuppressWarnings("unchecked")
    @GetMapping("/venues")
    public String getVenueListing(Model model) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "GET");
        jsonObject.put("identifier", "getVenueListing");
        jsonObject.put("uri", "/venues");

        ResponseEntity<String> response = listener.forwardToPAR(jsonObject.toString());
        if (response == null) return "error";

        JSONObject res = jsonService.convertStringToJSONObj(response.getBody());

        String value = jsonService.searchForValueJSO("results", res);
        List<JSONObject> venues = jsonService.convertStringToJSONArray(value);
       
        // // USE THIS TO CHECK FORMAT OF VENUES
        // System.out.println(venues.get(0).get("neighborhood"));
        
        model.addAttribute("listOfVenues", "View Available Venues");

        // CHANGE THIS
        model.addAttribute("getAllVenues", venueService.getListOfVenues());
        model.addAttribute("getVenueJSOS", venues);

        return "venue";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/venues/{venue_id}/book/{date}/time/{time}/party_size/{party_size}/first_name/{first_name}/last_name/{last_name}/phone/{phone}")
    public String bookVenue(Model model, 
            @PathVariable(name = "venue_id") String venue_id,
            @PathVariable(name = "date") String date,
            @PathVariable(name = "time") String time,
            @PathVariable(name = "party_size") String party_size,
            @PathVariable(name = "first_name") String first_name,
            @PathVariable(name = "last_name") String last_name,
            @PathVariable(name = "phone") String phone) {
        JSONObject venues = new JSONObject();
        venues.put("venue_id", venue_id);
        venues.put("date", date);
        venues.put("time", time);
        venues.put("party_size", party_size);
        venues.put("first_name", first_name);
        venues.put("last_name", last_name);
        venues.put("phone", phone);

        String uri = String.format("/venues/%s/book/%s/time/%s/party_size/%s/first_name/%s/last_name/%s/phone/%s", venue_id, date, time, party_size, first_name, last_name, phone);

        // Prepare request body for API
        Map<String, String> dataToForward = new HashMap<>();
        dataToForward.put("date", date);
        dataToForward.put("time", time);
        dataToForward.put("party_size", party_size);
        dataToForward.put("first_name", first_name);
        dataToForward.put("last_name", last_name);
        dataToForward.put("phone", phone);

        // Prepare request to send to PAR
        String uriToForward = "/venues/" + venue_id + "/book";
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("action", "PUT");
        jsonMap.put("identifier", "bookVenue");
        jsonMap.put("uri", uriToForward);
        jsonMap.put("data", dataToForward);
        JSONObject jsonToForward = new JSONObject(jsonMap);

        System.out.println("RAR forwarding data to PAR: " + jsonToForward.toString());
        
        ResponseEntity<String> response = listener.forwardToPAR(jsonToForward.toString());
        if (response == null) return "errorBookVenue";

        System.out.println(response);

        // check the points
        // if(insufficient points){
        //     return "errorBookVenue";
        // }

        // display for confirmation
        model.addAttribute("CBV_venue_id", "venue_id: " + venue_id);
        model.addAttribute("CBV_date", "date: " + date);
        model.addAttribute("CBV_time", "time: "+ time);
        model.addAttribute("CBV_part_size", "party_size: "+ party_size);
        model.addAttribute("CBV_first_name", "first_name: "+ first_name);
        model.addAttribute("CBV_last_name", "last_name: "+ last_name);
        model.addAttribute("CBV_phone", "phone: " + phone);

        return "confirmVenue";
    }

    @RequestMapping("/venues/{venue_id}/book/{date}/time/{time}/party_size/{party_size}/first_name/{first_name}/last_name/{last_name}/phone/{phone}/confirm")
    public String confirmBookVenue(@PathVariable(name = "venue_id") String venue_id,
            @PathVariable(name = "date") String date,
            @PathVariable(name = "time") String time,
            @PathVariable(name = "party_size") String party_size,
            @PathVariable(name = "first_name") String first_name,
            @PathVariable(name = "last_name") String last_name,
            @PathVariable(name = "phone") String phone) {
        
        JSONObject venues = new JSONObject();
        venues.put("venue_id", venue_id);
        venues.put("date", date);
        venues.put("time", time);
        venues.put("party_size", party_size);
        venues.put("first_name", first_name);
        venues.put("last_name", last_name);
        venues.put("phone", phone);

        String uri = String.format("/venues/%s/book/%s/time/%s/party_size/%s/first_name/%s/last_name/%s/phone/%s/confirm", venue_id, date, time, party_size, first_name, last_name, phone);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "POST");
        jsonObject.put("identifier", "confirmBookVenue");
        jsonObject.put("venues", venues);
        jsonObject.put("uri", uri);
        System.out.println("confirmBookVenue: " + jsonObject);

        String jsonString = jsonObject.toString();
        // listener.forwardToPAR(jsonString);

        // check the points

        // display for confirmation

        return "successBookVenue";
    }

}
