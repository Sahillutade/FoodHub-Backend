package com.example.foodhub_backend.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.foodhub_backend.model.MenuItem;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    @Aggregation(pipeline = {

    // ✅ FILTER menu items by restaurantId FIRST
    "{ $match: { restaurantId: ?0 } }",

    // Join restaurant using STRING comparison
    "{ $lookup: { " +
        "from: 'restaurant', " +
        "let: { rid: '$restaurantId' }, " +
        "pipeline: [" +
            "{ $match: { $expr: { $eq: [ { $toString: '$_id' }, '$$rid' ] } } }" +
        "], " +
        "as: 'restaurant' " +
    "} }",

    // Flatten restaurant array
    "{ $unwind: '$restaurant' }",

    // Select required fields
    "{ $project: { " +
        "_id: 0, " +
        "id: { $toString: '$_id' }, " +
        "itemName: 1, " +
        "description: 1, " +
        "category: 1, " +
        "price: 1, " +
        "image: 1, " +
        "restaurant: { " +
            "id: '$restaurantId', " +
            "name: '$restaurant.name' " +
        "} " +
    "} }"
    })
    List<Map<String, Object>> findMenuItemsWithRestaurantByRestaurantId(String restaurantId);

    @Aggregation(pipeline = {

    "{ $match: { $or: [ " +
        "{ itemName: { $regex: ?0, $options: 'i' } }, " +
        "{ description: { $regex: ?0, $options: 'i' } } ] } }",

    "{ $lookup: { from: 'restaurant', localField: 'restaurantId', foreignField: '_id', as: 'restaurant' } }",

    "{ $unwind: { path: '$restaurant', preserveNullAndEmptyArrays: true } }",

    "{ $project: { itemName: 1, description: 1, price: 1, category: 1, image: 1, mealTime: 1, restaurant: { _id: 1, name: 1 } } }"
    })
    List<Map<String, Object>> searchMenuItemsWithRestaurant(String searchQuery);

    long countByRestaurantId(String restaurantId);

    @Aggregation(pipeline = {

    "{ $addFields: { restaurantObjId: { $convert: { input: '$restaurantId', to: 'objectId', onError: null, onNull: null } } } }",

    "{ $lookup: { from: 'restaurant', localField: 'restaurantObjId', foreignField: '_id', as: 'restaurant' } }",

    "{ $unwind: { path: '$restaurant', preserveNullAndEmptyArrays: true } }",

    "{ $project: { " +
        "_id: 0, " +
        "id: { $toString: '$_id' }, " +
        "itemName: 1, description: 1, price: 1, image: 1, category: 1, mealTime: 1, " +
        "restaurant: { " +
            "id: { $toString: '$restaurant._id' }, " +
            "name: '$restaurant.name' " +
        "} " +
    "} }"
    })
    List<Map<String, Object>> findMenuItemswithRestaurant();

    @Aggregation(pipeline = {

    "{ $match: { $expr: { $eq: ['$_id', { $convert: { input: ?0, to: 'objectId', onError: null, onNull: null } }] } } }",

    "{ $addFields: { restaurantObjId: { $convert: { input: '$restaurantId', to: 'objectId', onError: null, onNull: null } } } }",

    "{ $lookup: { from: 'restaurant', localField: 'restaurantObjId', foreignField: '_id', as: 'restaurant' } }",

    "{ $unwind: { path: '$restaurant', preserveNullAndEmptyArrays: true } }",

    "{ $project: { " +
        "id: { $toString: '$_id' }, " +
        "itemName: 1, description: 1, price: 1, image: 1, category: 1, mealTime: 1, " +
        "restaurant: { id: { $toString: '$restaurant._id' }, name: '$restaurant.name' } " +
    "} }"
    })
    Optional<Map<String, Object>> findMenuItemWithRestaurantById(String menuItemId);

}
