package com.example.foodDelivery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodDelivery.model.MenuItem;
import com.example.foodDelivery.model.Restaurant;
import com.example.foodDelivery.repository.MenuItemRepository;
import com.example.foodDelivery.repository.RestaurantRepository;

@RestController
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	private MenuItemRepository menuRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepos;
	
	@GetMapping("/list")
	public List<MenuItem> getAllMenu(@RequestParam(required = false) String search){
		
		if(search == null || search.trim().isEmpty()) {
			return menuRepository.findAll();
		}
		return menuRepository.searchMenuitems(search);
		
	}
	
	@GetMapping("/{id}/menu-items")
	public Optional<MenuItem> getMenuItems(@PathVariable Long id){
		
		return menuRepository.findById(id);
		
	}
	
	@GetMapping("/restaurant/{restaurant_id}")
	public List<MenuItem> getMenuItemByRestaurant(@PathVariable Long restaurant_id){
		return menuRepository.findByRestaurant_Id(restaurant_id);
	}
	
	@PostMapping("/add-item")
	public ResponseEntity<?> saveMenuItem(@RequestBody Map<String, Object> body){
		
		Long restaurantId = Long.valueOf(body.get("restaurantId").toString());
		
		Restaurant restaurant = restaurantRepos.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));
		
		Object mealTimeObj = body.get("mealTime");
		
		List<String> mealTime;
		if(mealTimeObj instanceof String) {
			mealTime = Arrays.stream(mealTimeObj.toString().split(",")).map(String::trim).toList();
		}
		else {
			mealTime = (List<String>) mealTimeObj;
		}
		
		MenuItem menu = new MenuItem();
		menu.setItemName((String) body.get("itemName"));
		menu.setDescription((String) body.get("description"));
		menu.setPrice(Double.valueOf(body.get("price").toString()));
		menu.setCategory((String) body.get("category"));
		menu.setImage((String) body.get("image"));
		menu.setMealTime(mealTime);
		menu.setRestaurant(restaurant);
		
		menuRepository.save(menu);
		return ResponseEntity.status(HttpStatus.CREATED).body("Menu item saved");

	}
	
	@PutMapping("/edit-item/{id}")
	public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody Map<String, Object> body){
		
		try {
			MenuItem menu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu item not found"));
			
			Object mealTimeObj = body.get("mealTime");
			
			List<String> mealTime;
			if(mealTimeObj instanceof String) {
				mealTime = Arrays.stream(mealTimeObj.toString().split(",")).map(String::trim).toList();
			}
			else {
				mealTime = (List<String>) mealTimeObj;
			}
			
			menu.setItemName((String) body.get("itemName"));
			menu.setDescription((String) body.get("description"));
			menu.setPrice(Double.valueOf(body.get("price").toString()));
			menu.setCategory((String) body.get("category"));
			menu.setImage((String) body.get("image"));
			menu.setMealTime(mealTime);
			
			menuRepository.save(menu);
		
			return ResponseEntity.ok("Menu item updated successfully");
		}
		catch(Exception e) {
			return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(e.getMessage());
		}
		
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteMenuItem(@PathVariable Long id){
	
		try {
			
			menuRepository.deleteById(id);
			
			return ResponseEntity.ok("Menu item deleted Successfully");
			
		}
		catch(Exception e) {
			return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(e.getMessage());
		}
		
	}
	
}
