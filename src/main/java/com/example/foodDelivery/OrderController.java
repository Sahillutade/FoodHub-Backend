package com.example.foodDelivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodDelivery.model.Order;
import com.example.foodDelivery.model.OrderItem;
import com.example.foodDelivery.model.Users;
import com.example.foodDelivery.repository.OrderRepository;
import com.example.foodDelivery.repository.UsersRepository;
import com.example.foodDelivery.services.BrevoEmailService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private UsersRepository userRepo;
	
	@Autowired
	private BrevoEmailService brevoEmailService;
	
	@Transactional
	@PostMapping("/confirm-payment")
	public ResponseEntity<?> confirmPayment(@RequestBody Order order){
		
		if(order.getUser() == null || order.getUser().getId() == null) {
			return ResponseEntity.badRequest().body("User is missing in order");
		}
		
		Long userId = order.getUser().getId();
		Users user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		
		order.setUser(user);
		
		for(OrderItem item : order.getOrderItems()) {
			item.setOrder(order);
		}
		
		Order savedOrder = orderRepo.save(order);
		
		try {
	        sendOrderConfirmationMail(savedOrder);
	    } catch (Exception e) {
	        System.err.println("Mail failed: " + e.getMessage());
	    }
		
		return ResponseEntity.ok(savedOrder);
		
	}

	@GetMapping("/stats/{id}")
	public ResponseEntity<Map<String, Object>> getOrderStats(@PathVariable Long id){
		
		Object result = orderRepo.getOrderStatsByUser(id);
		
		Object[] stats = (Object[]) result;
		
		Map<String, Object> response = new HashMap<>();
		response.put("totalOrders", stats[0]);
		response.put("totalSpent", stats[1]);
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/user/{id}")
	public List<Order> getOrderByUsers(@PathVariable Long id){
		
		return orderRepo.findOrderWithItemsByUserId(id);
		
	}
	
	@GetMapping("/restaurant/{name}")
	public List<Order> getOrdersByRestaurant(@PathVariable String name){
		return orderRepo.findOrdersByRestaurantName(name);
	}
	
	private void sendOrderConfirmationMail(Order order) {
		
		Users user = order.getUser();
		if(user == null || user.getEmail() == null) {
			throw new IllegalStateException("User email not available");
		}
		
		StringBuilder mailBody = new StringBuilder();
		
		mailBody.append("Hi ")
		.append(user.getName() != null ? user.getName() : "Customer")
		.append(",\n\n")
		.append("Your order has been successfully placed.\n\n")
		.append("Order ID: ").append(order.getId()).append("\n")
		.append("Restaurant ID: ").append(order.getRestaurant().getId()).append("\n\n")
        .append("Order Items:\n");
		
		double total = 0;
	    for (OrderItem item : order.getOrderItems()) {
	        double itemTotal = item.getPrice() * item.getQuantity();
	        total += itemTotal;

	        mailBody.append("- ")
	                .append(item.getItemName())
	                .append(" x ")
	                .append(item.getQuantity())
	                .append(" = ₹")
	                .append(itemTotal)
	                .append("\n");
	    }

	    mailBody.append("\nTotal Amount: ₹").append(total);
	    mailBody.append("\n\nThank you for ordering with FoodHub!");

	    brevoEmailService.sendEmail(
	            user.getEmail(),
	            "Order Confirmed | Order #" + order.getId(),
	            mailBody.toString()
	    );
		
	}
	
}
