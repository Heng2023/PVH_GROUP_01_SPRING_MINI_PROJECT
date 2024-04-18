package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;


@Mapper
public interface ExpenseRepository {

    @Select("""
    INSERT INTO categories (name, description, user_id, category_id)
    VALUES (#{expense.name}, #{expense.description}, #{userId}, #{categoryId})
    RETURNING *
    """)
    @Results(id = "expenseMapping", value = {
            @Result(property = "expenseId", column = "expense_id"),

            @Result(property = "user", column = "user_id",
            one = @One(select = "org.example.expensetracking.repository.UserRepository.findUserById")),

            @Result(property = "category", column = "category_id",
            one = @One(select = "org.example.expensetracking.repository.CategoryRepository.findCategoryById"))
    })
    Expense saveExpense(@Param("expense") ExpenseRequest expenseRequest);
}
