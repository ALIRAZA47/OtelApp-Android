package com.example.otelapp.models;

public class Room {
    public String roomNumber;
    public String currentStatus;
    public String roomFare;
    public String roomDetail;

    //constructors

    public Room() {
    }

    public Room(String roomNumber, String currentStatus, String roomFare, String roomDetail) {
        this.roomNumber = roomNumber;
        this.currentStatus = currentStatus;
        this.roomFare = roomFare;
        this.roomDetail = roomDetail;
    }

    //to string

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                '}';
    }
}
