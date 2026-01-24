package com.example.foodhub_backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodhub_backend.model.Order;
import com.example.foodhub_backend.model.OrderItem;
import com.example.foodhub_backend.model.Restaurant;
import com.example.foodhub_backend.model.Users;
import com.example.foodhub_backend.repository.OrderRepository;
import com.example.foodhub_backend.repository.RestaurantRepository;
import com.example.foodhub_backend.repository.UsersRepository;
import com.example.foodhub_backend.services.BrevoEmailService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private BrevoEmailService brevoEmailService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody Order order){
        if (order.getUserId() == null) {
            return ResponseEntity.badRequest().body("User ID is missing in order");
        }

        Users users = usersRepository.findById(order.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"));

        double totalAmount = 0;
        if (order.getItems() != null) {
            for(OrderItem item : order.getItems()){
                totalAmount += item.getPrice() * item.getQuantity();
            }
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        try{
            sendOrderConfirmationMail(savedOrder, users);
        }
        catch(Exception e){
            System.err.println("Mail failed: " + e.getMessage());
        }

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<Map<String, Object>> getOrderStats(@PathVariable String id){
        List<Map<String, Object>> result = orderRepository.getOrderStatsByUser(id);

        Map<String, Object> response = new HashMap<>();
        response.put("orderCount", 0);
        response.put("totalAmount", 0);

        if (!result.isEmpty()) {
            response.put("orderCount", result.get(0).get("orderCount"));
            response.put("totalAmount", result.get(0).get("totalAmount"));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public List<Order> getOrdersByUser(@PathVariable String id){
        return orderRepository.findByUserIdOrderByCreatedAtDesc(id);
    }

    @GetMapping("/restaurant/{name}")
    public ResponseEntity<?> getOrdersByRestaurant(@PathVariable String name){
        Restaurant restaurant = restaurantRepository.findByNameIgnoreCase(name)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        List<Order> orders = orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurant.getId());

        return ResponseEntity.ok(orders);
    }

    private void sendOrderConfirmationMail(Order order, Users user){

        if(user.getEmail() == null){
            throw new IllegalStateException("User email not available");
        }

        StringBuilder mailBody = new StringBuilder();

        mailBody.append("Hi ")
        .append(user.getName() != null ? user.getName() : "Customer")
        .append(",\n\n")
        .append("Your order has been successfully placed.\n\n")
        .append("Order ID: ").append(order.getId()).append("\n")
        .append("Restaurant ID: ").append(order.getRestaurantId()).append("\n\n")
        .append("Order Items:\\n");

        double total = 0;

        for(OrderItem item : order.getItems()){
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

        mailBody.append("\n Total Amount: ₹").append(total);
        mailBody.append("\n\nThank you for ordering with FoodHub!");

        brevoEmailService.sendEmail(user.getEmail(), "Order Confirmed | Order #" + order.getId(), mailBody.toString());

    }

}
