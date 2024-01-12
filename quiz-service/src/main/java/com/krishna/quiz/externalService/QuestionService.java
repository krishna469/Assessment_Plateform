package com.krishna.quiz.externalService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.krishna.quiz.errorHandler.CustomErrorDecoder;
import com.krishna.quiz.utility.SuccessResponse;

@FeignClient(name = "QUESTION-SERVICE",configuration = CustomErrorDecoder.class)
public interface QuestionService {

	@DeleteMapping("/api/questions/deletebyquiz/{quizId}")
    ResponseEntity<SuccessResponse> deleteQuestionsByQuizId(@PathVariable int quizId);
}
