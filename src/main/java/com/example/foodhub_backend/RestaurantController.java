package com.example.foodhub_backend;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodhub_backend.model.Restaurant;
import com.example.foodhub_backend.repository.MenuItemRepository;
import com.example.foodhub_backend.repository.OrderRepository;
import com.example.foodhub_backend.repository.RestaurantRepository;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/list")
    public List<Restaurant> getAllRestaurant(){
        return restaurantRepository.findAll();
    }

    @GetMapping("/details/{name}")
    public ResponseEntity<?> getRestaurantByName(@PathVariable String name){
        return restaurantRepository.findByName(name)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/{name}")
    public Map<String, Object> getRestaurantStats(@PathVariable String name){

        Restaurant restaurant = restaurantRepository.findByNameIgnoreCase(name)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        long totalOrders = orderRepository.countByRestaurantId(restaurant.getId());

        double totalRevenue = orderRepository.getTotalRevenueByRestaurantName(name);

        long totalMenuItems = menuItemRepository.countByRestaurantId(restaurant.getId());

        return Map.of(
            "totalOrders", totalOrders,
            "totalRevenue", totalRevenue,
            "totalMenuItems", totalMenuItems
        );

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Restaurant loginRestaurant){

        Restaurant dbRestaurant = restaurantRepository.findByEmail(loginRestaurant.getEmail());

        if(dbRestaurant == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Restaurant-user not found"));
		}
		
		if(!dbRestaurant.getPassword().equals(loginRestaurant.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Password"));
		}

        return ResponseEntity.ok(
				Map.of(
						"message", "Login Successful",
						"name", dbRestaurant.getName()
				)
		);

    }

    @PostMapping("/register")
    public ResponseEntity<?> saveRestaurant(@RequestBody Restaurant restaurant){

        try{
            restaurantRepository.save(restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body("Restaurant registered");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}
