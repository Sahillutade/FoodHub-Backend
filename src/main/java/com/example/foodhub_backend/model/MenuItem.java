package com.example.foodhub_backend.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menu")
public class MenuItem {
    
    @Id
    private String id;

    private String itemName;
    private String description;
    private Double price;
    private String category;
    private String image;
    private List<String> mealTime;

    private String restaurantId;

    public MenuItem(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getMealTime() {
        return mealTime;
    }

    public void setMealTime(List<String> mealTime) {
        this.mealTime = mealTime;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public MenuItem(String id, String itemName, String description, Double price, String category, String image,
            List<String> mealTime, String restaurantId) {
        
        super();
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.mealTime = mealTime;
        this.restaurantId = restaurantId;
    }

}
