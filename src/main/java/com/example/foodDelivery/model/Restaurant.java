package com.example.foodDelivery.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurant")
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String email;
	private String phone;
	private String address;
	private String password;
	private String image;
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	private LocalTime opens;
	private LocalTime closes;
	
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<MenuItem> menuItems = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	public Restaurant(Long id, String name, String email, String phone, String address, String password,
			LocalTime opens, LocalTime closes, String image, List<MenuItem> menuItems) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.password = password;
		this.opens = opens;
		this.closes = closes;
		this.image = image;
		this.menuItems = menuItems;
	}

	public Restaurant() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
