package com.example.foodDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodDelivery.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
