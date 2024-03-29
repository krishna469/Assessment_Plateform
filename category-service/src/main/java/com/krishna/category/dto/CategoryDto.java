package com.krishna.category.dto;


import com.krishna.category.utility.ValidationMessage;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class representing a category.
 */
@Setter
@Getter
@NoArgsConstructor
public class CategoryDto {

    /**
     * The ID of the category.
     */
    private int categoryId;

    /**
     * The name of the category.
     */
    @NotBlank(message = ValidationMessage.CATEGORY_NAME_EMPTY)
    private String categoryName;

    /**
     * The description of the category.
     */
    @NotBlank(message = ValidationMessage.DESCRIPTION_EMPTY)
    private String description;

    /**
     * The State of the category.
     */
    private boolean enabled;

    /**
     * Copy constructor for the CategoryDto class.
     *@param cId The CategoryDto object to create a copy from.
     * @param cName The CategoryDto object to create a copy from.
     *@param cDescription The CategoryDto object to create a copy from.
     */
    public CategoryDto(final int cId, final String cName,
            final String cDescription) {
        super();
        this.categoryId = cId;
        this.categoryName = cName;
        this.description = cDescription;
    }
}
