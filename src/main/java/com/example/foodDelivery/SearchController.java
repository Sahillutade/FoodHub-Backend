package com.example.foodDelivery;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.foodDelivery.repository.MenuItemRepository;
import com.example.foodDelivery.repository.RestaurantRepository;

@RestController
@RequestMapping("/api")
public class SearchController {

	@Autowired
	private MenuItemRepository menuRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepo;
	
	@GetMapping("/search")
	public Map<String, Object> search(@RequestParam String query){
		
		Map<String, Object> result = new HashMap<>();
		result.put("restaurants", restaurantRepo.searchRestaurants(query));
		result.put("dishes", menuRepository.searchMenuitems(query));
		
		return result;
		
	}
	
}
