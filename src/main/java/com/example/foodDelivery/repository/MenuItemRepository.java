package com.example.foodDelivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.foodDelivery.model.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

	List<MenuItem> findByRestaurant_Id(Long restaurant_id);
	
	@Query(""" 
			select m from MenuItem m
			where lower(m.itemName) like lower(concat('%', :search, '%'))
			or lower(m.description) like lower(concat('%', :search, '%'))
	""")
	List<MenuItem> searchMenuitems(@Param("search") String search);
	
}
