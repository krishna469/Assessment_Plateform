package com.krishna.quiz.service;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.krishna.quiz.dto.QuizDto;
import com.krishna.quiz.exception.ResourceNotFoundException;
import com.krishna.quiz.utility.SuccessResponse;

/**
 * Service interface for managing quizzes.
 */
public interface QuizService {

    /**
     * Adds a new quiz.
     * @param quizDTO The DTO containing quiz information.
     * @return A message indicating the result of the operation.
     */
    SuccessResponse addQuiz(QuizDto quizDTO);

    /**
     * Deletes a quiz.
     * @param quizId The ID of the quiz to delete.
     * @return response to in string format.
     * @throws NotFoundException If the quizId is invalid.
     */
    SuccessResponse deleteQuiz(Integer quizId) throws NotFoundException;

    boolean deleteQuizzesByCategoryId(int categoryId);
    /**
     * Retrieves a quiz by its ID.
     * @param quizId The ID of the quiz to retrieve.
     * @return The retrieved quizDto.
     * @throws NotFoundException If the quizId is invalid.
     */
    QuizDto getQuizById(Integer quizId) throws NotFoundException;



    /**
     * Updates a quiz.
     * @param quizId The update the quiz information.
     * @param quizDTO The updated quiz information.
     * @return The updated quizDto.
     * @throws NotFoundException If the quizId is invalid.
     */
    SuccessResponse updateQuiz(Integer quizId, QuizDto quizDTO
            )throws NotFoundException;

//    /**
//     * Updates a category.
//     * @param quizId to get quiz by category .
//     * @return The List Of Questions  Entity.
//     */
//    List<QuestionDto> getAllQuestionByQuiz(int quizId);

    /**
     * Enables a category with the specified ID.
     * @param quizId The unique identifier of the category to enable.
     */
    void enableQuiz(int quizId);

    /**
     * Disables a category with the specified ID.
     * @param quizId The unique identifier of the category to disable.
     */
    void disableQuiz(int quizId);

	/**
	 * Retrieves a quiz by ID.
	 *
	 * @param quizId The ID of the quiz to retrieve.
	 * @return The quizDto representing the quiz.
	 * @throws RuntimeException If the quiz is not found.
	 */
	List<QuizDto> getQuizByCategoryId(Integer categoryId) throws ResourceNotFoundException;

	/**
	 * Retrieves a list of all quizzes.
	 *
	 * @return A list of CategoryDto objects representing quizzes.
	 */
	List<QuizDto> getAllQuizzes();
}
