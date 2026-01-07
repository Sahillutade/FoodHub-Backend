package com.example.foodDelivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodDelivery.model.Admin;
import com.example.foodDelivery.model.Order;
import com.example.foodDelivery.model.OrderItem;
import com.example.foodDelivery.repository.AdminRepository;
import com.example.foodDelivery.repository.OrderRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Admin loginAdmin){
		
		Admin dbAdmin = adminRepo.findByEmail(loginAdmin.getEmail());
		
		if(dbAdmin == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Admin not found"));
		}
		
		if(!dbAdmin.getPassword().equals(loginAdmin.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Password"));
		}
		
		return ResponseEntity.ok(
				Map.of(
						"message", "Login Successful",
						"name", dbAdmin.getName()
				)
		);
		
	}
	
	@GetMapping("/stats")
	public Map<String, Object> getAdminDashboardStats(){
		
		List<Object[]> result = orderRepo.getAdminDashboardStats();
		
		Object[] row = result.get(0);
		
		Map<String, Object> response = new HashMap<>();
		response.put("totalUsers", ((Number) row[0]).longValue());
	    response.put("totalRestaurants", ((Number) row[1]).longValue());
	    response.put("totalOrders", ((Number) row[2]).longValue());
	    response.put("totalRevenue", ((Number) row[3]).doubleValue());
        
        return response;
		
	}
	
	@GetMapping("/orders")
    public List<Map<String, Object>> getAllOrders() {

        List<Order> orders = orderRepo.findAllOrdersWithItemsandRestaurant();

        List<Map<String, Object>> response = new ArrayList<>();

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {

                Map<String, Object> data = new HashMap<>();
                data.put("orderId", order.getId());
                data.put("restaurantName", order.getRestaurant().getName());
                data.put("itemName", item.getItemName());
                data.put("price", item.getPrice());

                response.add(data);
            }
        }
        return response;
    }
	
}
