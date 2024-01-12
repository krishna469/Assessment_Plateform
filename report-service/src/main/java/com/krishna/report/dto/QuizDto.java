package com.krishna.report.dto;

import org.springframework.validation.annotation.Validated;

import com.krishna.report.utility.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class representing a quiz.
 */
@Validated
@Setter
@Getter
@NoArgsConstructor
public class QuizDto {

    /**
     * The ID of the quiz.
     */
    private int quizId;
    
    /**
     * The ID of the category.
     */
    private int categoryId;

    /**
     * The name of the quiz.
     */
    @NotBlank(message = ValidationMessage.QUIZ_NAME_EMPTY)
    private String quizName;

    /**
     * The Description of the quiz.
     */
    @NotBlank(message = ValidationMessage.QUIZ_DESCRIPTION_EMPTY)
    private String quizDescription;

    /**
     * The time of the quiz.
     */
    @Min(value = 1, message = ValidationMessage.TIME_MINIMUM)
    private int timeInMinutes;

    /**
     * The state of the quiz.
     */
    private boolean enabled;
    /**
     * The category belong to quiz.
     */
    
    private CategoryDto category;


}
