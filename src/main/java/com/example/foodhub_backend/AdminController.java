package com.example.foodhub_backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodhub_backend.dto.AdminOrderStatsDTO;
import com.example.foodhub_backend.model.Admin;
import com.example.foodhub_backend.repository.AdminRepository;
import com.example.foodhub_backend.repository.OrderRepository;
import com.example.foodhub_backend.repository.RestaurantRepository;
import com.example.foodhub_backend.repository.UsersRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin loginAdmin){

        Admin dbAdmin = adminRepository.findByEmail(loginAdmin.getEmail());

        if(dbAdmin == null){
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
    public ResponseEntity<Map<String, Object>> getAdminDashboardStats(){
        Map<String, Object> response = new HashMap<>();

        AdminOrderStatsDTO orderStats = orderRepository.getAdminOrderStats();

        if (orderStats != null) {
            response.put("totalOrders", orderStats.getTotalOrders());
            response.put("totalRevenue", orderStats.getTotalRevenue());
        } else {
            response.put("totalOrders", 0);
            response.put("totalRevenue", 0.0);
        }

        response.put("totalUsers", usersRepository.count());
        response.put("totalRestaurants", restaurantRepository.count());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public List<Map<String, Object>> getAllOrders(){
        List<Map<String, Object>> orders = orderRepository.findOrdersWithRestaurantName();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Map<String, Object> order : orders) {

            List<Map<String, Object>> items =
                (List<Map<String, Object>>) order.get("items");

            if (items == null) continue;

            for (Map<String, Object> item : items) {

                Map<String, Object> data = new HashMap<>();

                data.put("orderId", order.get("_id"));
                data.put("restaurantName", order.get("restaurantName"));
                data.put("itemName", item.get("itemName"));
                data.put("itemPrice", item.get("price"));

                // ORDER TOTAL (same for all items in that order)
                data.put("orderTotal", order.get("totalAmount"));

                response.add(data);
            }
        }

        return response;
    }

}
