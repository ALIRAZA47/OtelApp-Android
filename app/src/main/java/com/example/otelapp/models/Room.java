package com.example.otelapp.models;

public class Room {
    public String roomNumber;
    public String currentStatus;

    //constructor

    public Room(String roomNumber, String currentStatus) {
        this.roomNumber = roomNumber;
        this.currentStatus = currentStatus;
    }

    public Room() {
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
