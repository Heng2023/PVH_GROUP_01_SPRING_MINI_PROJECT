package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.configuration.UUIDTypeHandler;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ExpenseRepository {
    @Select("""
        INSERT 
        INTO expenses (amount, description, date, user_id, category_id)
        VALUES (#{expense.amount}, #{expense.description},#{expense.date}, #{userId}, #{expense.categoryId})
        RETURNING *
    """)
    @Results(id = "expenseMapping", value = {
            @Result(property = "name", column = "name"),
            @Result(property = "expenseId", column = "expense_id"),
            @Result(property = "description", column = "description"),
            @Result(property = "categoryID", column = "category_id", typeHandler = UUIDTypeHandler.class,
                    one = @One(select = "org.example.expensetracking.repository.CategoryRepository.findCategoryById"))
    })
    Expense saveExpense(@Param("expense") ExpenseRequest expenseRequest, UUID userId);

    @Select("""
        UPDATE expenses set amount = #{amount} , description = #{description} , category_id = #{categoryId}
        WHERE user_id = #{userId} AND  expense_id = #{expenseId}
        RETURNING *, expense_id
    """)
    @ResultMap("expenseMapping")
    Expense updateExpense(UUID userId, ExpenseRequest expenseRequest);

    @Select("""
        SELECT * FROM expenses
        WHERE user_id = #{userId} AND expense_id = #{expenseId}
    """)
    @ResultMap("expenseMapping")
    Expense getExpenseById(UUID userId, UUID expenseId);


    @Delete("""
        DELETE FROM expenses
        WHERE user_id = #{userId} AND expense_id = #{expenseId}
    """)
    void deleteExpense(UUID userId, UUID expenseId);

    @Select("""
        SELECT * 
        FROM expenses
    """)
    @ResultMap("expenseMapping")
    List<Expense> findAllExpense();
}