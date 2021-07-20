package com.example.otelapp.models;

import java.util.ArrayList;

public class HotelDetails {
    public Hotel hotelOfThisRoom;
    public String description;
    public ArrayList<Room> rooms = new ArrayList<>();

    //constructors
    public HotelDetails() {
    }

    public HotelDetails(Hotel hotelOfThisRoom, String description, ArrayList<Room> rooms) {
        this.hotelOfThisRoom = hotelOfThisRoom;
        this.description = description;
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "HotelDetails{" +
                "hotelOfThisRoom=" + hotelOfThisRoom +
                ", description='" + description + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
