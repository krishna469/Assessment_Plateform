package com.krishna.quiz.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.krishna.quiz.dto.CategoryDto;
import com.krishna.quiz.dto.QuizDto;
import com.krishna.quiz.entity.Quiz;
import com.krishna.quiz.exception.DuplicateResourceException;
import com.krishna.quiz.exception.ResourceNotFoundException;
import com.krishna.quiz.externalService.CategoryService;
import com.krishna.quiz.externalService.QuestionService;
import com.krishna.quiz.repository.QuizRepository;
import com.krishna.quiz.service.QuizService;
import com.krishna.quiz.utility.Message;
import com.krishna.quiz.utility.SuccessResponse;



/**
 * Implementation of the CategoryService interface for managing categories.
 */
@Service
public class QuizServiceImpl implements QuizService {

    /**
     * This is Quiz Repository object that is for calling. the repository.
     * methods.
     */
    @Autowired
    private QuizRepository quizRepository;

    /**
     * This is use to map the category with Dto and viceversa..
     */
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuestionService questionService;
    
    @Autowired
	private RestTemplate restTemplate;
    /**
     * this is logger object that is use to generate log.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QuizServiceImpl.class);

    /**
     * Adds a new quiz.
     *
     * @param quizDTO The DTO containing category information.
     * @return A message indicating the result of the operation.
     */
    @Override
    public final SuccessResponse addQuiz(final QuizDto quizDTO) {

        Optional<Quiz> existingQuiz = quizRepository.findByQuizName(quizDTO.getQuizName());
        if (existingQuiz.isPresent()) {
            LOGGER.error(Message.QUIZ_ALREADY_EXISTS);
            throw new DuplicateResourceException(Message.QUIZ_ALREADY_EXISTS);
        }

        Optional<CategoryDto> categoryResponse = Optional.ofNullable(categoryService.getCategoryById(quizDTO.getCategoryId()));

        if (categoryResponse.isEmpty()) {
            LOGGER.error(Message.CATEGORY_NOT_FOUND + quizDTO.getCategoryId());
            throw new ResourceNotFoundException(Message.CATEGORY_NOT_FOUND + quizDTO.getCategoryId());
        }
    	  
          Quiz quiz = convertToEntity(quizDTO);
          quizRepository.save(quiz);
          return new SuccessResponse(HttpStatus.CREATED.value(), Message.QUIZ_CREATED_SUCCESSFULLY);
      
    }


    /**
     * Updates a quiz.
     * @param quizId  The ID of the quiz.
     * @param quizDTO The DTO containing updated quiz information.
     * @return The updated String.
     */
    @Override
    public final SuccessResponse updateQuiz(final Integer quizId,
            final QuizDto quizDTO)
            throws ResourceNotFoundException {
        Quiz existingQuiz = quizRepository.findById(quizId).orElseGet(() -> {
            LOGGER.error(Message.QUIZ_NOT_FOUND + quizId);
            throw new ResourceNotFoundException(
                    Message.QUIZ_NOT_FOUND + quizId);
        });
        if (!existingQuiz.getQuizName().equals(quizDTO.getQuizName())
                && quizRepository.findByQuizName(quizDTO.getQuizName())
                        .isPresent()) {
            LOGGER.error(Message.QUIZ_ALREADY_EXISTS);
            throw new DuplicateResourceException(Message.QUIZ_ALREADY_EXISTS);
        }

        Optional<CategoryDto> categoryResponse = Optional.ofNullable(categoryService.getCategoryById(quizDTO.getCategory().getCategoryId()));

        CategoryDto category = categoryResponse.orElseThrow(() -> {
            LOGGER.error(Message.CATEGORY_NOT_FOUND + quizDTO.getCategory().getCategoryId());
            return new ResourceNotFoundException(Message.CATEGORY_NOT_FOUND + quizDTO.getCategory().getCategoryId());
        });
        
        existingQuiz.setQuizName(quizDTO.getQuizName());
        existingQuiz.setQuizDescription(quizDTO.getQuizDescription());
        existingQuiz.setTimeInMinutes(quizDTO.getTimeInMinutes());
        existingQuiz.setCategory(category);
        quizRepository.save(existingQuiz);
        
        return new SuccessResponse(HttpStatus.OK.value(),
                Message.QUIZ_UPDATED_SUCCESSFULLY);


    }

    /**
     * Deletes a quiz.
     *
     * @param quizId The ID of the quiz to delete.
     *
     */
    @Override
    public final SuccessResponse deleteQuiz(final Integer quizId)
            throws ResourceNotFoundException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> {
                    LOGGER.error(Message.QUIZ_NOT_FOUND, quizId);
                    return new ResourceNotFoundException(
                            Message.QUIZ_NOT_FOUND + quizId);
                });
        
        questionService.deleteQuestionsByQuizId(quizId);
        
        quizRepository.deleteById(quiz.getQuizId());
        return new SuccessResponse(HttpStatus.OK.value(),
                Message.QUIZ_DELETED_SUCCESSFULLY);
    }
    
    public boolean deleteQuizzesByCategoryId(int categoryId) {
        List<Quiz> quizzes = quizRepository.findByCategoryId(categoryId);

        for (Quiz quiz : quizzes) {
            quizRepository.delete(quiz);
            questionService.deleteQuestionsByQuizId(quiz.getQuizId());
        }
        return true;
    }

    /**
     * Retrieves a quiz by ID.
     *
     * @param quizId The ID of the quiz to retrieve.
     * @return The quizDto representing the quiz.
     * @throws RuntimeException If the quiz is not found.
     */
    @Override
    public final QuizDto getQuizById(final Integer quizId)
            throws ResourceNotFoundException {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> {
            LOGGER.error("Quiz not found with Id : {}", quizId);
            return new ResourceNotFoundException(
                    "Quiz with ID " + quizId + " not found");
        });
        quiz.setCategory(categoryService.getCategoryById(quiz.getCategoryId()));
        return convertToDto(quiz);
    }

    
    /**
     * Retrieves a quiz by ID.
     *
     * @param quizId The ID of the quiz to retrieve.
     * @return The quizDto representing the quiz.
     * @throws RuntimeException If the quiz is not found.
     */
    @Override
    public final List<QuizDto> getQuizByCategoryId(final Integer categoryId)
            throws ResourceNotFoundException {
        List<Quiz> quizzes = quizRepository.findByCategoryId(categoryId);
        return quizzes.stream().map(quiz -> {
            CategoryDto categoryDto = 
            		categoryService.getCategoryById(quiz.getCategoryId());

            QuizDto quizDto = convertToDto(quiz);
            quizDto.setCategory(categoryDto);
            return quizDto;
        })
        .collect(Collectors.toList());
    }
    
    
    
    /**
     * Retrieves a list of all quizzes.
     *
     * @return A list of CategoryDto objects representing quizzes.
     */
    @Override
    public final List<QuizDto> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return quizzes.stream()
                .map(quiz -> {
                    CategoryDto categoryDto = 
                    		categoryService.getCategoryById(quiz.getCategoryId());

                    QuizDto quizDto = convertToDto(quiz);
                    quizDto.setCategory(categoryDto);
                    return quizDto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Converts a quiz entity to a QuizDTO.
     *
     * @param quiz The quiz entity to convert.
     * @return The converted QuizDTO.
     */
    QuizDto convertToDto(final Quiz quiz) {
        QuizDto quizDTO = modelMapper.map(quiz, QuizDto.class);
        return quizDTO;
    }

    /**
     * Converts a quizDTO to a quiz entity.
     *
     * @param quizDTO The quizDTO to convert.
     * @return The converted quiz entity.
     */
    Quiz convertToEntity(final QuizDto quizDto) {
        Quiz quiz = modelMapper.map(quizDto, Quiz.class);
        return quiz;
    }

    /**
     * Converts a quizDTO to a quiz entity.
     *
     * @param quizId To find the questions.
     * @return The list of question entity.
     */
//    @Override
//    public final List<QuestionDto> getAllQuestionByQuiz(final int quizId) {
//
//        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
//        if (optionalQuiz.isEmpty()) {
//            throw new ResourceNotFoundException(Message.QUIZ_NOT_FOUND
//                    + quizId);
//        }
//        List<Question> questions = optionalQuiz.get().getQuestions();
//        return questions.stream().map(this::convertEntityToDto)
//                .collect(Collectors.toList());
//    }

    
    /**
     * Enables a category with the specified ID.
     * @param quizId The unique identifier of the category to enable.
     */
    public void enableQuiz(int quizId) {
        Quiz existingQuiz = quizRepository.findById(quizId)
               .orElseThrow(() -> {
                    LOGGER.error(Message.QUIZ_NOT_FOUND + quizId);
                    throw new ResourceNotFoundException(
                          Message.QUIZ_NOT_FOUND + quizId);
                  });

            existingQuiz.setEnabled(true);
            quizRepository.save(existingQuiz);
        
    }

    /**
     * Disables a category with the specified ID.
     * @param quizId The unique identifier of the category to disable.
     */
    public void disableQuiz(int quizId) {
        Quiz existingQuiz = quizRepository.findById(quizId)
                .orElseThrow(() -> {
                     LOGGER.error(Message.QUIZ_NOT_FOUND + quizId);
                     throw new ResourceNotFoundException(
                           Message.QUIZ_NOT_FOUND + quizId);
                   });

            existingQuiz.setEnabled(false);
            quizRepository.save(existingQuiz);
    }

//    /**
//     * @param question The object to be converted.
//     * @return the converted into QuestionDto entity.
//     */
//    private QuestionDto convertEntityToDto(final Question question) {
//
//        QuestionDto questionDto = new QuestionDto();
//        questionDto.setQuestionId(question.getQuestionId());
//        questionDto.setQuestionText(question.getQuestionText());
//        QuestionOptions options = new QuestionOptions(question.getOptionOne(),
//                question.getOptionTwo(), question.getOptionThree(),
//                question.getOptionFour(), question.getCorrectOption());
//        questionDto.setOptions(options);
//        CategoryDto categoryDto = new CategoryDto();
//        categoryDto.setCategoryId(
//                question.getQuiz().getCategory().getCategoryId());
//        categoryDto.setCategoryName(
//                question.getQuiz().getCategory().getCategoryName());
//        categoryDto.setDescription(
//                question.getQuiz().getCategory().getDescription());
//
//        QuizDTO quizDto = new QuizDTO();
//        quizDto.setQuizId(question.getQuiz().getQuizId());
//        quizDto.setQuizName(question.getQuiz().getQuizName());
//        quizDto.setQuizDescription(question.getQuiz().getQuizDescription());
//        quizDto.setTimeInMinutes(question.getQuiz().getTimeInMinutes());
//        quizDto.setCategory(categoryDto);
//
//        questionDto.setQuiz(quizDto);
//
//        return questionDto;
//    }

}
