package com.krishna.report.externalService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.krishna.report.dto.RegistrationDto;
import com.krishna.report.errorHandler.CustomErrorDecoder;


@FeignClient(name = "REGISTRATION-SERVICE",configuration = CustomErrorDecoder.class)
public interface RegistrationService {

	  @GetMapping("/users/get/{userId}")
	  RegistrationDto getUserById(@PathVariable("userId") int userId);
	  
	  @GetMapping("/users/getUser/{emailId}")
	  RegistrationDto getUser(@PathVariable("emailId") String emailId);
}
