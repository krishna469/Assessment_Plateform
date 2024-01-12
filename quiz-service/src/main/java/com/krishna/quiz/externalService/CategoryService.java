package com.krishna.quiz.externalService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.krishna.quiz.dto.CategoryDto;
import com.krishna.quiz.errorHandler.CustomErrorDecoder;

@FeignClient(name = "CATEGORY-SERVICE",configuration = CustomErrorDecoder.class)
public interface CategoryService {

	@GetMapping("/category/{categoryId}")
	CategoryDto getCategoryById(@PathVariable("categoryId") int categoryId);
	
}

