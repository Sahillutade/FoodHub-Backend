package com.example.foodhub_backend.dto;

public class AdminOrderStatsDTO {

    private int totalOrders;
    private double totalRevenue;
    public int getTotalOrders() {
        return totalOrders;
    }
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
    public double getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public AdminOrderStatsDTO() {
        
    }

}
