package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;

import java.util.List;
import java.util.UUID;


@Mapper
public interface ExpenseRepository {
    //Insert Expenses
    @Select("""
    INSERT INTO expenses (amount, description, date, user_id, category_id)
    VALUES (#{expense.amount}, #{expense.description},#{expense.date}, #{userId}, #{expense.categoryId})
    RETURNING *
    """)
    @Result(property = "categoryId", column = "category_id")
    ExpenseRequest saveExpense(@Param("expense") ExpenseRequest expenseRequest, UUID userId);

    //Update Expenses
    @Select("""
    UPDATE expenses set amount = #{amount} , description = #{description} , category_id = #{categoryId}
    WHERE user_id = #{userId} AND  expense_id = #{expenseId}
    RETURNING *
    """)
    @Result(property = "categoryId", column = "category_id")
    Expense updateExpense(UUID userId, ExpenseRequest expenseRequest);

    //Get  Expenses By Id
    @Select("""
    SELECT * FROM expenses
    WHERE user_id = #{userId} AND expense_id = #{expenseId}
    """)
    Expense getExpenseById(UUID userId, UUID expenseId);

    //Delete Expenses
    @Delete("""
    DELETE FROM expenses
    WHERE user_id = #{userId} AND expense_id = #{expenseId}
    """)
    void deleteExpense(UUID userId, UUID expenseId);

    //Get All Expenses
    @Select("""
    SELECT * FROM expenses
    """)
    @Results(id = "expanseMapping",value = {
            @Result(property = "expenseId", column = "expense_id"),
            @Result(property = "user", column = "user_id",
                    one = @One(select = "org.example.expensetracking.repository.UserRepository.findUserById")
            ),
            @Result(property = "category", column = "category_id",
                    one = @One(select = "org.example.expensetracking.repository.CategoryRepository.findCategoryById")
            ),

    })
    List<Expense> findAllExpense();


}
