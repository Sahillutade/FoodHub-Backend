package com.example.foodDelivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.foodDelivery.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	Optional<Restaurant> findByName(String name);
	
	@Query("""
			select r from Restaurant r
			where lower(r.name) like lower(concat('%', :search, '%'))
	""")
	List<Restaurant> searchRestaurants(@Param("search") String search);
	
	Restaurant findByEmail(String email);
	
	@Query("""
			SELECT
      (SELECT COUNT(o.id)
       FROM Order o
       WHERE o.restaurant.id = 
            (SELECT r.id FROM Restaurant r WHERE LOWER(r.name) = LOWER(:name))),

      (SELECT COALESCE(SUM(oi.price), 0)
       FROM OrderItem oi
       WHERE oi.order.restaurant.id =
            (SELECT r.id FROM Restaurant r WHERE LOWER(r.name) = LOWER(:name))),

      (SELECT COUNT(m.id)
       FROM MenuItem m
       WHERE m.restaurant.id =
            (SELECT r.id FROM Restaurant r WHERE LOWER(r.name) = LOWER(:name)))
	""")
	Object[][] getRestaurantStats(@Param("name") String name);
	
}
