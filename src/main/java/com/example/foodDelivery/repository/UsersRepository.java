package com.example.foodDelivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodDelivery.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByName(String name);
	
	Users findByEmail(String email);
	
}
