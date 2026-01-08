package com.example.foodDelivery;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodDelivery.dto.LoginRequest;
import com.example.foodDelivery.dto.RegisterRequest;
import com.example.foodDelivery.model.Users;
import com.example.foodDelivery.repository.UsersRepository;
import com.example.foodDelivery.services.BrevoEmailService;

@RestController
@RequestMapping("/user")
public class UsersController {

	@Autowired
	private UsersRepository userRepository;
	
	private BrevoEmailService brevoEmailService;
	
	private final Map<String, String> otpMap = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> otpExpiryMap = new ConcurrentHashMap<>();
	
	private String generateOtp() {
		return String.valueOf(100000 + new Random().nextInt(900000));
	}
	
	@GetMapping("/user-list")
	public List<Users> getAllUser(){
		
		return userRepository.findAll();
		
	}
	
	@GetMapping("/details/{name}")
	public ResponseEntity<?> getUserByName(@PathVariable String name) {
		
		return userRepository.findByName(name)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginUser){
		
		Users dbUser = userRepository.findByEmail(loginUser.getEmail());
		
		if(dbUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
		}
		
		if(!dbUser.getPassword().equals(loginUser.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Password"));
		}
		
		return ResponseEntity.ok(
				Map.of(
						"message", "Login Successful",
						"name", dbUser.getName()
				)
		);
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> saveUsers(@RequestBody RegisterRequest request){
		try {
			
			Users user = new Users();
			user.setName(request.getName());
	        user.setEmail(request.getEmail());
	        user.setPassword(request.getPassword());
	        user.setPhone(request.getPhone());
	        user.setAddress(request.getAddress());
	        user.setGender(request.getGender());
			
	        userRepository.save(user);
			return ResponseEntity.status(HttpStatus.CREATED).body("User Registered");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/send-otp")
	public Map<String, Object> sendOtp(@RequestBody Map<String, String> body){
		
		String email = body.get("email");
		
		String otp = generateOtp();
		
		otpMap.put(email, otp);
		otpExpiryMap.put(email, LocalDateTime.now().plusMinutes(5));
		
		String mailText = "Your OTP is: " + otp + "\nValid for 5 minutes.";
		
		brevoEmailService.sendEmail(email, "OTP Verification From FoodHub", mailText);
		
		Map<String, Object> response = new HashMap<>();
		response.put("otp", otp);
		response.put("message", "OTP sent successfully");
		
		return response;
		
	}
	
}
