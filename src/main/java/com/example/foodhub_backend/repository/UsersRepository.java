package com.example.foodhub_backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foodhub_backend.model.Users;



public interface UsersRepository extends MongoRepository<Users, String> {

    Optional<Users> findByName(String name);

    Users findByEmail(String email);

}
