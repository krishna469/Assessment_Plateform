package com.krishna.category.entity;


import java.util.ArrayList;
import java.util.List;

import com.krishna.category.dto.QuizDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a category.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@SequenceGenerator(name = "catSeq",
initialValue = Category.ID_INITIAL_VALUE, allocationSize = 1)
public class Category {

    /**
     * Constant for initial value of category ID sequence.
     */
    public static final int ID_INITIAL_VALUE = 3010;
    /**
     * The ID of the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "catSeq")
    private int categoryId;

    /**
     * The name of the category.
     */
    @Column(nullable = false)
    private String categoryName;

    /**
     * The description of the category.
     */
    @Column(nullable = false)
    private String description;
    
    /**
     * The description of the category.
     */
    @Transient
	private List<QuizDto> quizess=new ArrayList<QuizDto>();

    /**
     * The state of the category.
     */
    @Column(nullable = false)
    private boolean enabled;
 
}
