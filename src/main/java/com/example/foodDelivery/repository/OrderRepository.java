package com.example.foodDelivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.foodDelivery.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("""
					select count(distinct o.id) , coalesce(sum(oi.price * oi.quantity), 0)
					from Order o
					join o.orderItems oi
					where o.user.id = :id
	""")
	Object getOrderStatsByUser(@Param("id") Long id);
	
	@Query(""" 
				select distinct o from Order o join fetch o.orderItems oi where o.user.id = :id
	""")
	List<Order> findOrderWithItemsByUserId(@Param("id") Long id);
	
	@Query("""
			SELECT o
	        FROM Order o
	        JOIN FETCH o.orderItems oi
	        JOIN o.restaurant r
	        WHERE LOWER(r.name) = LOWER(:name)
	""")
	List<Order> findOrdersByRestaurantName(@Param("name") String name);
	
	@Query("""
			SELECT
	            (SELECT COUNT(u.id) FROM Users u),
	            (SELECT COUNT(r.id) FROM Restaurant r),
	            (SELECT COUNT(o.id) FROM Order o),
	            (SELECT COALESCE(SUM(oi.price), 0) FROM OrderItem oi)
	""")
	List<Object[]> getAdminDashboardStats();
	
	@Query("""
	        SELECT DISTINCT o
	        FROM Order o
	        JOIN FETCH o.orderItems oi
	        JOIN FETCH o.restaurant r
	""")
	List<Order> findAllOrdersWithItemsandRestaurant();
	
}
