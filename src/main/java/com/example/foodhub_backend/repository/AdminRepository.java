package com.example.foodhub_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foodhub_backend.model.Admin;


public interface AdminRepository extends MongoRepository<Admin, String> {

    Admin findByEmail(String email);

}
