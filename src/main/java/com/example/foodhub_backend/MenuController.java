package com.example.foodhub_backend;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodhub_backend.model.MenuItem;
import com.example.foodhub_backend.repository.MenuItemRepository;
import com.example.foodhub_backend.repository.RestaurantRepository;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/list")
    public List<Map<String, Object>> getAllMenu(@RequestParam(required = false) String search){
        
        return menuItemRepository.findMenuItemswithRestaurant();

    }

    @GetMapping("/{id}/menu-items")
    public ResponseEntity<?> getMenuItems(@PathVariable String id){
        return menuItemRepository.findMenuItemWithRestaurantById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<Map<String, Object>> getMenuItemByRestaurant(@PathVariable String restaurantId){
        return menuItemRepository.findMenuItemsWithRestaurantByRestaurantId(restaurantId);
    }

    @PostMapping("/add-item")
    public ResponseEntity<?> saveMenuItem(@RequestBody Map<String, Object> body){
        String restaurantId = String.valueOf(body.get("restaurantId").toString());

        restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Object mealTimeObj = body.get("mealTime");
        List<String> mealTime;

        if (mealTimeObj instanceof String) {
            // when frontend sends: "All"
            mealTime = List.of(mealTimeObj.toString());
        } 
        else if (mealTimeObj instanceof List<?>) {
            // when frontend sends: ["Breakfast", "Lunch"]
            mealTime = ((List<?>) mealTimeObj).stream()
            .map(String::valueOf)
            .toList();
        } 
        else {
            throw new IllegalArgumentException("Invalid mealTime format");
        }

        MenuItem menu = new MenuItem();
        menu.setItemName((String) body.get("itemName"));
        menu.setDescription((String) body.get("description"));
        menu.setPrice(Double.valueOf(body.get("price").toString()));
        menu.setCategory((String) body.get("category"));
        menu.setImage((String) body.get("image"));
        menu.setMealTime(mealTime);
        menu.setRestaurantId(restaurantId);

        menuItemRepository.save(menu);

        return ResponseEntity.status(HttpStatus.CREATED)
        .body("Menu item saved");
    }

    @PutMapping("/edit-item/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable String id, @RequestBody Map<String, Object> body){
        try{
            MenuItem menu = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu item not found"));

            @SuppressWarnings("unchecked")
            List<String> mealTime = (List<String>) body.get("mealTime");

            menu.setItemName((String) body.get("itemName"));
			menu.setDescription((String) body.get("description"));
			menu.setPrice(Double.valueOf(body.get("price").toString()));
			menu.setCategory((String) body.get("category"));
			menu.setImage((String) body.get("image"));
			menu.setMealTime(mealTime);

            menuItemRepository.save(menu);

            return ResponseEntity.ok("Menu item updated successfully");
        }
        catch(Exception e){
            return ResponseEntity
	        .status(HttpStatus.INTERNAL_SERVER_ERROR)
	        .body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable String id){

        try{
            menuItemRepository.deleteById(id);

            return ResponseEntity.ok("Menu item deleted Successfully");
        }
        catch(Exception e){
            return ResponseEntity
	        .status(HttpStatus.INTERNAL_SERVER_ERROR)
	        .body(e.getMessage());
        }

    }

}
