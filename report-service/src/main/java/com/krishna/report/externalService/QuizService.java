package com.krishna.report.externalService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.krishna.report.dto.QuizDto;
import com.krishna.report.errorHandler.CustomErrorDecoder;

@FeignClient(name = "QUIZ-SERVICE",configuration = CustomErrorDecoder.class)
public interface QuizService {

	@GetMapping("/quizzes/{quizId}")
    QuizDto getQuizById(@PathVariable final Integer quizId);
	
}
