package com.example.foodhub_backend;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodhub_backend.dto.LoginRequest;
import com.example.foodhub_backend.dto.RegisterRequest;
import com.example.foodhub_backend.model.Users;
import com.example.foodhub_backend.repository.UsersRepository;
import com.example.foodhub_backend.services.BrevoEmailService;


@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BrevoEmailService brevoEmailService;

    private final Map<String, String> otpMap = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> otpExpiryMap = new ConcurrentHashMap<>();
	
	private String generateOtp() {
		return String.valueOf(100000 + new Random().nextInt(900000));
	}

    @GetMapping("/user-list")
    public List<Users> getAllUser(){

        return usersRepository.findAll();

    }

    @GetMapping("/details/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name){
        return usersRepository.findByName(name)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginUser){

        Users dbUsers = usersRepository.findByEmail(loginUser.getEmail());

        if (dbUsers == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }

        if (!dbUsers.getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Password"));
        }

        return ResponseEntity.ok(
				Map.of(
						"message", "Login Successful",
						"name", dbUsers.getName()
				)
		);

    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody RegisterRequest request){
        try{
            Users user = new Users();
			user.setName(request.getName());
	        user.setEmail(request.getEmail());
	        user.setPassword(request.getPassword());
	        user.setPhone(request.getPhone());
	        user.setAddress(request.getAddress());
	        user.setGender(request.getGender());
			
	        usersRepository.save(user);
			return ResponseEntity.status(HttpStatus.CREATED).body("User Registered");
        }
        catch(Exception e){
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
