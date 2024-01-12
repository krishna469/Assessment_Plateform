package com.krishna.category.externalServices;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.krishna.category.dto.QuizDto;
import com.krishna.category.utility.SuccessResponse;

import java.util.List;

@FeignClient(name="quiz-service")
public interface QuizService {
	
	  @GetMapping("/quizzes/categories/{categoryId}")
	  List<QuizDto> getQuizzesByCategoryId(@PathVariable("categoryId") int categoryId);

	  @DeleteMapping("/quizzes/delete/quiz/{categoryId}")
	  ResponseEntity<String> deleteQuizzesByCategoryId(@PathVariable int categoryId);
}
