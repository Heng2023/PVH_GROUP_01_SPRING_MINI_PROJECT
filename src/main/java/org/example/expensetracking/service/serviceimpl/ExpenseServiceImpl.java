package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.ExpenseRequest;
import org.example.expensetracking.model.dto.response.CategoryExpenseResponse;
import org.example.expensetracking.model.dto.response.CategoryResponse;
import org.example.expensetracking.model.dto.response.ExpenseResponse;
import org.example.expensetracking.model.dto.response.UserResponse;
import org.example.expensetracking.repository.CategoryRepository;
import org.example.expensetracking.repository.ExpenseRepository;
import org.example.expensetracking.service.CategoryService;
import org.example.expensetracking.service.ExpenseService;
import org.example.expensetracking.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final UserService userService;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, CategoryService categoryService, UserService userService, UserService userService1, CategoryService categoryService1, CategoryRepository categoryRepository, CategoryService categoryService2, ModelMapper modelMapper) {
        this.expenseRepository = expenseRepository;
        this.userService = userService1;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService2;
        this.modelMapper = modelMapper;
    }

    @Override
    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, UUID userId) {
        // Fetch the Category object directly using the category ID from the request
        Category category = categoryRepository.getCategoryById(expenseRequest.getCategoryId(), userId);
        if (category == null) {
            throw new IllegalArgumentException("Category with ID " + expenseRequest.getCategoryId() + " does not exist for user ID " + userId);
        }

        // Map the request to an Expense object
        Expense expense = modelMapper.map(expenseRequest, Expense.class);

        // Fetch the User object
        User user = userService.findUserById(userId);
        expense.setUser(user);

        // Set the Category object to the Expense
        expense.setCategory(category);

        // Save the Expense and get the inserted Expense object
        Expense insertedExpense = expenseRepository.save(expense);

        // Map the inserted Expense to ExpenseResponse and return
        ExpenseResponse expenseResponse = modelMapper.map(insertedExpense, ExpenseResponse.class);
        System.out.println(expenseResponse.getExpenseId());
        // Set the UserResponse and CategoryResponse
        expenseResponse.setUser(modelMapper.map(user, UserResponse.class));
        expenseResponse.setCategory(modelMapper.map(category, CategoryExpenseResponse.class));

        return expenseResponse;
    }

    @Override
    public void deleteExpense(UUID expenseId) {
        // Call the ExpenseRepository method to delete the expense
        expenseRepository.deleteExpenseById(expenseId);

    }

    @Override
    public ExpenseResponse getExpenseById(UUID expenseId) {
        // Fetch the expense from the repository by its ID
        Expense expense = expenseRepository.getExpenseById(expenseId);

        if (expense == null) {
            // If expense not found, throw IllegalArgumentException
            throw new IllegalArgumentException("Expense with ID " + expenseId + " not found");
        }

        // Map the expense to ExpenseResponse and return
        return modelMapper.map(expense, ExpenseResponse.class);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAllExpenses();
        return expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse updateExpense(UUID expenseId, ExpenseRequest expenseRequest) {
        // Fetch the existing expense by its ID
        Expense existingExpense = expenseRepository.getExpenseById(expenseId);

        if (existingExpense == null) {
            // If expense not found, throw IllegalArgumentException
            throw new IllegalArgumentException("Expense with ID " + expenseId + " not found");
        }

        // Update the existing expense with the new details from the request
        existingExpense.setAmount(expenseRequest.getAmount());
        existingExpense.setDescription(expenseRequest.getDescription());
        existingExpense.setDate(expenseRequest.getDate());

        // Fetch the Category object directly using the category ID from the request
        Category category = categoryRepository.getCategoryById(expenseRequest.getCategoryId(), existingExpense.getUser().getUserId());
        if (category == null) {
            throw new IllegalArgumentException("Category with ID " + expenseRequest.getCategoryId() + " does not exist for user ID " + existingExpense.getUser().getUserId());
        }

        existingExpense.setCategory(category);

        // Update the Expense using the updateExpense method
        Expense updatedExpense = expenseRepository.updateExpense(existingExpense);

        // Map the updated Expense to ExpenseResponse and return
        ExpenseResponse expenseResponse = modelMapper.map(updatedExpense, ExpenseResponse.class);
        expenseResponse.setUser(modelMapper.map(updatedExpense.getUser(), UserResponse.class));
        expenseResponse.setCategory(modelMapper.map(updatedExpense.getCategory(), CategoryExpenseResponse.class));

        return expenseResponse;
    }

}
