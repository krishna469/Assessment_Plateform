package com.krishna.category.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a question in a quiz.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    /**
     * The ID of the question.
     */
    private int questionId;

    /**
     * The text of the question.
     */
    private String questionText;

    /**
     * The first option for the question.
     */
    private String optionOne;

    /**
     * The second option for the question.
     */
    private String optionTwo;

    /**
     * The third option for the question.
     */
    private String optionThree;

    /**
     * The forth option for the question.
     */
    private String optionFour;

    /**
     * The correct option for the question.
     */
    private String correctOption;

}
