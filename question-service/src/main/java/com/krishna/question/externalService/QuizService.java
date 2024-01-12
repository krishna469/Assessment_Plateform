package com.krishna.question.externalService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.krishna.question.dto.QuizDto;
import com.krishna.question.errorHandler.CustomErrorDecoder;



@FeignClient(name = "QUIZ-SERVICE",configuration = CustomErrorDecoder.class)
public interface QuizService {

	@GetMapping("quizzes/{quizId}")
	QuizDto getQuizById(@PathVariable final Integer quizId);
}
