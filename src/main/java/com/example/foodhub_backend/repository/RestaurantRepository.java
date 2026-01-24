package com.example.foodhub_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foodhub_backend.model.Restaurant;



public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    Optional<Restaurant> findByName(String name);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    Restaurant findByEmail(String email);

    Optional<Restaurant> findByNameIgnoreCase(String name);

}
