package com.krishna.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.krishna.auth.entity.UserCrediential;
import com.krishna.auth.repository.UserCrediantialRepository;

@Service
public class UserCrediantialService {

	@Autowired
	private UserCrediantialRepository userCrediantialRepository;
	
	 @Autowired
	 private JwtService jwtService;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	    
	public String saveUser(UserCrediential userCrediential) {
		
		userCrediential.setPassword(passwordEncoder.encode(userCrediential.getPassword()));
		userCrediantialRepository.save(userCrediential);
		
		return "user creadiential saved successfully";
	}
	
	 public String generateToken(String username) {
	        return jwtService.generateToken(username);
	 }
	 

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
	
}
