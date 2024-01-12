package com.krishna.question.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krishna.question.entity.Question;


/**
 * Repository interface for managing Question entities.
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {


    /**
     * Finds a Quiz entity by Quiz Name.
     * @param quiz The category Object to search for.
     * @return A List containing the found Questions entity, if any.
     */
	List<Question> findByQuizId(int quizId);
//    List<Question> findByQuiz(Quiz quiz);
}
