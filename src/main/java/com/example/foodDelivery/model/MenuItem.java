package com.example.foodDelivery.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_menu_items")
public class MenuItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "restaurant_id", nullable = false)
	@JsonIgnoreProperties("menuItems")
	private Restaurant restaurant;
	
	private String itemName;
	private String description;
	private Double price;
	private String category;
	private String image;
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Convert(converter = StringListConverter.class)
	private List<String> mealTime;

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

	public List<String> getMealTime() {
		return mealTime;
	}

	public void setMealTime(List<String> mealTime) {
		this.mealTime = mealTime;
	}

	public MenuItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MenuItem(Long id, Restaurant restaurant, String itemName, String description, Double price, String category,
			String image, List<String> mealTime) {
		super();
		this.id = id;
		this.restaurant = restaurant;
		this.itemName = itemName;
		this.description = description;
		this.price = price;
		this.category = category;
		this.image = image;
		this.mealTime = mealTime;
	}
	
}
