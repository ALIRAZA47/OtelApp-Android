package com.example.otelapp.models;

public class Room {
    public String roomNumber;
    public String currentStatus;
    public User residentOfRoomIfAny;

    //constructor

    public Room(String roomNumber, String currentStatus, User residentOfRoomIfAny) {
        this.roomNumber = roomNumber;
        this.currentStatus = currentStatus;
        this.residentOfRoomIfAny = residentOfRoomIfAny;
    }

    public Room() {
    }

    //to string

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                ", residentOfRoomIfAny=" + residentOfRoomIfAny +
                '}';
    }
}
