package com.example.otelapp.models;

public class User {
    public String name;
    public String email;
    public String phone;
    public String cnic;
    public String address;
    public String gender;

    public User() {};

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", cnic='" + cnic + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public User(String name, String email, String phone, String cnic, String address, String gender) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cnic = cnic;
        this.address = address;
        this.gender = gender;
    }
}

