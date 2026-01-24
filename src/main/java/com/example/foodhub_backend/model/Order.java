package com.example.foodhub_backend.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {
    
    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String restaurantId;

    private List<OrderItem> items;

    private String status = "DELIVERED";
    private Double totalAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    public Order() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Order(String id, String userId, String restaurantId, List<OrderItem> items, String status,
            Double totalAmount, LocalDateTime createdAt) {
        super();
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }    

}
