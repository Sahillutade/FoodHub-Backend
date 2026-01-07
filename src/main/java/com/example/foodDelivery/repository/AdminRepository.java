package com.example.foodDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodDelivery.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Admin findByEmail(String email);
	
}
