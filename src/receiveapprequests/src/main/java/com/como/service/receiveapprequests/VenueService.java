package com.como.service.receiveapprequests;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class VenueService {
    private static final Map<String, Venue> venues = new HashMap<>();
    private static final ArrayList<Venue> listOfVenue = new ArrayList<Venue>();

    public VenueService() {
        Venue kids21 = new Venue();
        kids21.setVenueName("Kids21");
        kids21.setVenueId("1");
        kids21.setArrivalTime("9:00 PM");
        kids21.setDuration(80);
        kids21.setVenuePointCost(1000);
        venues.put(kids21.getVenueId(), kids21);
        listOfVenue.add(kids21);

        Venue comoShambhala = new Venue();
        comoShambhala.setVenueName("COMO Shambhala");
        comoShambhala.setVenueId("2");
        comoShambhala.setArrivalTime("9:00 PM");
        comoShambhala.setDuration(90);
        comoShambhala.setVenuePointCost(2000);
        venues.put(comoShambhala.getVenueId(), comoShambhala);
        listOfVenue.add(comoShambhala);

        Venue hilton = new Venue();
        hilton.setVenueName("Hilton");
        hilton.setVenueId("3");
        hilton.setArrivalTime("9:00 PM");
        hilton.setVenuePointCost(3000);
        hilton.setDuration(40);
        venues.put(hilton.getVenueId(), hilton);
        listOfVenue.add(hilton);
    }

    public List<Venue> getAllVenues() {
        return new ArrayList(venues.values());
    }

    public Venue getVenue(String venueId) {
        return venues.get(venueId);
    }

    public void addVenue(Venue venue) {
        venues.put(venue.getVenueName(), venue);
    }

    public List<Venue> getListOfVenues(){
        ArrayList<Venue> listOfVenue2 = new ArrayList<Venue>();
        for(int i = 0; i< (listOfVenue.size()/2); i++){
            listOfVenue2.add(listOfVenue.get(i));
        }
        return listOfVenue2;
    }
}
