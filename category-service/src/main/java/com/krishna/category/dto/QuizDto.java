package com.krishna.category.dto;

import com.krishna.category.entity.Question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a quiz.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {

    /**
     * The ID of the quiz.
     */
    private int quizId;

    /**
     * The name of the quiz.
     */
    private String quizName;

    /**
     * The Description of the quiz .
     */
    private String quizDescription;

    /**
     * The time of the Quiz.
     */
    private int timeInMinutes;
    
    /**
     * The time of the Quiz.
     */
    private Question questions;

    /**
     * The state of the quiz.
     */
    private boolean enabled;

}
