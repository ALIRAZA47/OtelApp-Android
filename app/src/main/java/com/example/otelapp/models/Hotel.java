package com.example.otelapp.models;

import java.util.ArrayList;

public class Hotel {
    public String hotelName;
    public String hotelAddress;
    public int totalRooms;
    public ArrayList<Room> rooms = new ArrayList<>();

    public Hotel() {}

    public Hotel(String hotelName, String hotelAddress, int totalRooms) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.totalRooms = totalRooms;
        this.rooms = null;
    }

    public Hotel(String hotelName, String hotelAddress, int totalRooms, ArrayList<Room> rooms) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.totalRooms = totalRooms;
        this.rooms = rooms;
    }


    @Override
    public String toString() {
        return "Hotel{" +
                "hotelName='" + hotelName + '\'' +
                ", hotelAddress='" + hotelAddress + '\'' +
                ", totalRooms=" + totalRooms +
                ", rooms=" + rooms +
                '}';
    }
}
