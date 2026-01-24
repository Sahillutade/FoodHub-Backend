package com.example.foodhub_backend.model;

import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurant")
public class Restaurant {
    
    @Id
    private String id;

    private String name;

    @Indexed(unique  = true)
    private String email;
    
    private String phone;
    private String address;
    private String password;
    private String image;
    private LocalTime opens;
    private LocalTime closes;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public LocalTime getOpens() {
        return opens;
    }
    public void setOpens(LocalTime opens) {
        this.opens = opens;
    }
    public LocalTime getCloses() {
        return closes;
    }
    public void setCloses(LocalTime closes) {
        this.closes = closes;
    }
    
    public Restaurant(String id, String name, String email, String phone, String address, String password, String image, LocalTime opens, LocalTime closes){
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.image = image;
        this.opens = opens;
        this.closes = closes;
    }

    public Restaurant(){
        super();
    }

}
