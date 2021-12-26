package com.como.service.receiveapprequests;

import java.util.ArrayList;

public class Venue {
    private String venueName;
    private String venueId;
    private String arrivalTime;
    private int duration;
    private int venuePointCost;

    public String getVenueName(){
        return venueName;
    }
    public String getVenueId(){
        return venueId;
    }
    public String getArrivalTime(){
        return arrivalTime;
    }
    public int getDuration(){
        return duration;
    }
    public int venuePointCost(){
        return venuePointCost;
    }

    public void setVenueName(String venueName){
        this.venueName = venueName;    
    }
    public void setVenueId(String venueId){
        this.venueId = venueId;
    }
    public void setArrivalTime(String arrivalTime){
        this.arrivalTime = arrivalTime;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }
    public void setVenuePointCost(int venuePointCost){
        this.venuePointCost = venuePointCost;
    }
    
    public String stringToList(ArrayList<String> availTimeslots){
        String list = "";
		for (int i = 0; i < availTimeslots.size(); i++) {
			list += availTimeslots.get(i);
			list += ", ";
		}            
        return list;
    }
}
