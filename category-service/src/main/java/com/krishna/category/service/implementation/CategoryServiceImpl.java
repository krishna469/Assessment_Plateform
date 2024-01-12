package com.krishna.category.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.krishna.category.dto.CategoryDto;
import com.krishna.category.dto.QuizDto;
import com.krishna.category.entity.Category;
import com.krishna.category.exception.DuplicateResourceException;
import com.krishna.category.exception.ResourceNotFoundException;
import com.krishna.category.externalServices.QuizService;
import com.krishna.category.repository.CategoryRepository;
import com.krishna.category.service.CategoryService;
import com.krishna.category.utility.Message;
import com.krishna.category.utility.SuccessResponse;


/**
 * Implementation of the CategoryService interface for managing categories.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /**
     * This is Category Repository object that is for calling. the repository
     * methods.
     */
    @Autowired
    private CategoryRepository categoryRepository;

    
    /**
     * This is use to map the category with Dto and viceversa..
     */
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private QuizService quizService;

    /**
     * this is logger object that is use to generate log.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CategoryServiceImpl.class);

    /**
     * Adds a new category.
     * @param categoryDto The DTO containing category information.
     * @return A message indicating the result of the operation.
     */
    @Override
    public final SuccessResponse addCategory(final CategoryDto categoryDto) {

        Category newCategory = dtoToEntity(categoryDto);
        Optional<Category> existingCategory = categoryRepository
                .findByCategoryName(newCategory.getCategoryName());
        if (existingCategory.isPresent()) {
            LOGGER.error(Message.CATEGORY_ALREADY_EXISTS);
            throw new DuplicateResourceException(
                Message.CATEGORY_ALREADY_EXISTS);
        }
        categoryRepository.save(newCategory);
        return new SuccessResponse(HttpStatus.CREATED.value(),
                Message.CATEGORY_CREATED_SUCCESSFULLY);
    }

    /**
     * Retrieves a category by ID.
     * @param id The ID of the category to retrieve.
     * @return The CategoryDto representing the category.
     * @throws RuntimeException If the category is not found.
     */
    @Override
    public final CategoryDto getCategoryById(final int id) {

        Optional<Category> foundCategory = categoryRepository.findById(id);

        if (foundCategory.isPresent()) {
            Category category = foundCategory.get();
            return entityToDTO(category);
        } else {
            LOGGER.error(Message.CATEGORY_NOT_FOUND + id);
            throw new ResourceNotFoundException(
                    Message.CATEGORY_NOT_FOUND + id);
        }

    }

    /**
     * Retrieves a list of all categories.
     * @return A list of CategoryDto objects representing categories.
     */
    @Override
    public final List<CategoryDto> getAllCategory() {

        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates a category.
     * @param categoryDto The DTO containing updated category information.
     * @return The updated CategoryDto.
     */
    @Override
    public final SuccessResponse updateCategory(final CategoryDto categoryDto) {

        Category existingCategory = categoryRepository
                .findById(categoryDto.getCategoryId()).orElseThrow(() -> {
                    LOGGER.error(Message.CATEGORY_NOT_FOUND,
                            categoryDto.getCategoryId());
                    return new ResourceNotFoundException(
                            Message.CATEGORY_NOT_FOUND
                                    + categoryDto.getCategoryId());
                });
        if (!existingCategory.getCategoryName()
                .equals(categoryDto.getCategoryName())
                && categoryRepository
                        .findByCategoryName(categoryDto.getCategoryName())
                        .isPresent()) {
            LOGGER.error(Message.CATEGORY_ALREADY_EXISTS);
            throw new DuplicateResourceException(
                Message.CATEGORY_ALREADY_EXISTS);
        }
        existingCategory = dtoToEntity(categoryDto);
        categoryRepository.save(existingCategory);
        return new SuccessResponse(HttpStatus.OK.value(),
                Message.CATEGORY_UPDATED_SUCCESSFULLY);
    }

    /**
     * Deletes a category.
     * @param categoryId The ID of the category to delete.
     * @return A message indicating the result of the operation.
     */
    @Override
    public final SuccessResponse deleteCategory(final int categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    LOGGER.error(Message.CATEGORY_NOT_FOUND, categoryId);
                    return new ResourceNotFoundException(
                            Message.CATEGORY_NOT_FOUND + categoryId);
                });
        
        quizService.deleteQuizzesByCategoryId(categoryId);
        categoryRepository.delete(category);
        return new SuccessResponse(HttpStatus.OK.value(),
                Message.CATEGORY_DELETED_SUCCESSFULLY);
    }

 
    /**
     * Enables a category with the specified ID.
     * @param categoryId The unique identifier of the category to enable.
     */
    public boolean enableCategory(int categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                 LOGGER.error(Message.CATEGORY_NOT_FOUND + categoryId);
                  throw new ResourceNotFoundException(
                        Message.CATEGORY_NOT_FOUND + categoryId);
                });

            existingCategory.setEnabled(true);
            categoryRepository.save(existingCategory);
            return true;

    }

    /**
     * Disables a category with the specified ID.
     * @param categoryId The unique identifier of the category to disable.
     */
    public boolean disableCategory(int categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    LOGGER.error(Message.CATEGORY_NOT_FOUND + categoryId);
                    throw new ResourceNotFoundException(
                          Message.CATEGORY_NOT_FOUND + categoryId);
                  });

            existingCategory.setEnabled(false);
            categoryRepository.save(existingCategory);
            return false;
    }

    
    /**
     * Converts UserDTO to User entity.
     * @param categoryDto The UserDTO object.
     * @return Category object.
     */
    final Category dtoToEntity(final CategoryDto categoryDto) {
        return this.modelMapper.map(categoryDto, Category.class);
    }

    /**
     * Converts User entity to UserDTO.
     * @param category category object.
     * @return CategoryDto object.
     */
    final CategoryDto entityToDTO(final Category category) {
        return this.modelMapper.map(category, CategoryDto.class);
    }

}
