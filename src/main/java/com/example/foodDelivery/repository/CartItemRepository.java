package com.example.foodDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodDelivery.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
