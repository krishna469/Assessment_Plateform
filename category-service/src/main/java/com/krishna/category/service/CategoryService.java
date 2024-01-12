package com.krishna.category.service;


import java.util.List;
import org.springframework.stereotype.Service;

import com.krishna.category.dto.CategoryDto;
import com.krishna.category.dto.QuizDto;
import com.krishna.category.utility.SuccessResponse;



/**
 * Service interface for managing categories.
 */
@Service
public interface CategoryService {

    /**
     * Adds a new category.
     * @param categoryDto The DTO containing category information.
     * @return A message indicating the result of the operation.
     */
    SuccessResponse addCategory(CategoryDto categoryDto);

    /**
     * Retrieves a category by its ID.
     * @param id The ID of the category to retrieve.
     * @return The retrieved CategoryDto.
     */
    CategoryDto getCategoryById(int id);

    /**
     * Retrieves all categories.
     * @return A list of all CategoryDtos.
     */
    List<CategoryDto> getAllCategory();

    /**
     * Deletes a category.
     * @param categoryId The ID of the category to delete.
     * @return A message indicating the result of the operation.
     */
    SuccessResponse deleteCategory(int categoryId);

    /**
     * Updates a category.
     * @param categoryDto The updated category information.
     * @return The updated CategoryDto.
     */
    SuccessResponse updateCategory(CategoryDto categoryDto);
    
    /**
     * Enables a category with the specified ID.
     * @param categoryId The unique identifier of the category to enable.
     * @return 
     */
    boolean enableCategory(int categoryId);

    /**
     * Disables a category with the specified ID.
     * @param categoryId The unique identifier of the category to disable.
     */
    boolean disableCategory(int categoryId);
}
