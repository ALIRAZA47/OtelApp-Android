package com.example.otelapp.models;

public class Hotel {
    public String hotelName;
    public String hotelAddress;
    public int totalRooms;

    public Hotel() {
    }

    public Hotel(String hotelName, String hotelAddress, int totalRooms) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.totalRooms = totalRooms;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelName='" + hotelName + '\'' +
                ", hotelAddress='" + hotelAddress + '\'' +
                ", totalRooms=" + totalRooms +
                '}';
    }
}
