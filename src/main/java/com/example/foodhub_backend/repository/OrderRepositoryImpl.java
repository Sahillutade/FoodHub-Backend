package com.example.foodhub_backend.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.example.foodhub_backend.model.Restaurant;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Double getTotalRevenueByRestaurantName(String restaurantName){

        Restaurant restaurant = restaurantRepository.findByNameIgnoreCase(restaurantName)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(
                Criteria.where("restaurantId").is(restaurant.getId())
            ),
            Aggregation.unwind("items"),
            Aggregation.group().sum("items.price").as("total")
        );

        AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "orders", Document.class);

        Document doc = result.getUniqueMappedResult();
        return doc != null ? doc.getDouble("total") : 0.0;

    }

}
