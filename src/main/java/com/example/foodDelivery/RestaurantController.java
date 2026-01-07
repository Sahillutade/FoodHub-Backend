package com.example.foodDelivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodDelivery.model.Restaurant;
import com.example.foodDelivery.repository.RestaurantRepository;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

	@Autowired
	private RestaurantRepository restaurantRepo;
	
	@GetMapping("/list")
	public List<Restaurant> getAllRestaurant(){
		return restaurantRepo.findAll();
	}
	
	@GetMapping("/details/{name}")
	public ResponseEntity<?> getRestaurantByName(@PathVariable String name){
		return restaurantRepo.findByName(name)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/stats/{name}")
	public Map<String, Object> getRestaurantStats(@PathVariable String name){
		
		System.out.println("Restaurant name from URL = [" + name + "]");
		
		Object[][] result = restaurantRepo.getRestaurantStats(name);
		
		Object[] stats = result[0];
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("totalOrders", stats[0]);
		response.put("totalRevenue", stats[1]);
		response.put("totalMenuItems", stats[2]);
		
		return response;
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Restaurant loginRestaurant){
		
		Restaurant dbRestaurant = restaurantRepo.findByEmail(loginRestaurant.getEmail());
		
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
		
		try {
			restaurantRepo.save(restaurant);
			return ResponseEntity.status(HttpStatus.CREATED).body("Restaurant registered");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
}
