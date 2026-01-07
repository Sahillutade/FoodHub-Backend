package com.example.foodDelivery.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	@JsonBackReference
	private Order order;
	
	private String itemName;
	private Double price;
	private Integer quantity;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public OrderItem(Long id, Order order, String itemName, Double price, Integer quantity) {
		super();
		this.id = id;
		this.order = order;
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
