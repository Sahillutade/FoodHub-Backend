package com.example.foodhub_backend.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foodhub_backend.dto.AdminOrderStatsDTO;
import com.example.foodhub_backend.model.Order;
import java.util.List;
import java.util.Map;


public interface OrderRepository extends MongoRepository<Order, String>, OrderRepositoryCustom {

    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Order> findByRestaurantIdOrderByCreatedAtDesc(String restaurantId);

    /* User Order Stats */
    @Aggregation(pipeline = {
        "{ $match: { userId: ?0 } }",
        "{ $group: { " + 
            "_id: null, " + 
            "orderCount: { $sum: 1 }, " + 
            "totalAmount: { $sum: '$totalAmount' }" +  
        "} }",
        "{ $project: { _id: 0, orderCount: 1, totalAmount: 1 } }"
    })
    List<Map<String, Object>> getOrderStatsByUser(String userId);

    /* Admin Dashboard Stats */
    @Aggregation(pipeline = {
        "{ $group: { _id: null, totalOrders: { $sum: 1 }, totalRevenue: { $sum: '$totalAmount' } } }",
        "{ $project: { _id: 0, totalOrders: 1, totalRevenue: 1 } }"
    })
    AdminOrderStatsDTO getAdminOrderStats();

    Long countByRestaurantId(String restaurantId);

    @Aggregation(pipeline = {

    "{ $lookup: { from: 'restaurant', localField: 'restaurantObjectId', foreignField: '_id', as: 'restaurant' } }",

    "{ $unwind: '$items' }",

    "{ $unwind: '$restaurant' }",

    "{ $project: { " +
        "orderId: { $toString: '$_id' }, " +
        "itemName: '$items.itemName', " +
        "itemPrice: '$items.price', " +
        "orderTotal: '$totalAmount', " +
        "restaurantName: '$restaurant.name' " +
    "} }"
    })
    List<Map<String, Object>> findOrdersWithRestaurantName();

}
