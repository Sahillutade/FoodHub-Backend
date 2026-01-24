package com.example.foodhub_backend;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodhub_backend.model.Restaurant;
import com.example.foodhub_backend.repository.MenuItemRepository;
import com.example.foodhub_backend.repository.RestaurantRepository;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String q){

        List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCase(q);

        List<Map<String, Object>> menuItems = menuItemRepository.searchMenuItemsWithRestaurant(q);

        return Map.of(
            "restaurants", restaurants,
            "dishes", menuItems
        );

    }

}
