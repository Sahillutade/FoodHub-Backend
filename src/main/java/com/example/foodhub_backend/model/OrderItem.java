package com.example.foodhub_backend.model;

public class OrderItem {
    
    private String menuItemId;
    private String itemName;
    private Double price;
    private int quantity;
    
    public OrderItem() {
        super();
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderItem(String menuItemId, String itemName, Double price, int quantity) {
        super();
        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
