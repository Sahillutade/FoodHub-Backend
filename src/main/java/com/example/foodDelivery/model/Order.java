package com.example.foodDelivery.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnoreProperties({"password","orders"})
	private Users user;
	
	@OneToMany(
			mappedBy = "order",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.EAGER
	)
	@JsonManagedReference
	private List<OrderItem> orderItems = new ArrayList<>();
	
	private Double totalAmount;
	
	private String orderStatus = "DELIVERED";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(Long id, Restaurant restaurant, Users user, List<OrderItem> orderItems, Double totalAmount) {
		super();
		this.id = id;
		this.restaurant = restaurant;
		this.user = user;
		this.orderItems = orderItems;
		this.totalAmount = totalAmount;
	}
	
}
