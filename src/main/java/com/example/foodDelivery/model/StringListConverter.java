package com.example.foodDelivery.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		if(attribute == null || attribute.isEmpty()) {
			return "";
		}
		
		return String.join(",", attribute);
	}
	
	public List<String> convertToEntityAttribute(String dbData){
		if(dbData == null || dbData.isEmpty()) {
			return new ArrayList<>();
		}
		return Arrays.stream(dbData.split(",")).map(String::trim).collect(Collectors.toList());
	}
	
}