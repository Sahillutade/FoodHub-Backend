package com.example.foodhub_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin")
public class Admin {
    
    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    public Admin(){
        super();
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin(String id, String name, String email, String password){
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
