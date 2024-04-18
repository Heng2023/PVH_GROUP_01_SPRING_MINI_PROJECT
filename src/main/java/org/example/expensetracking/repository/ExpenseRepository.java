package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;

@Mapper
public interface ExpenseRepository {

    @Select("""
    INSERT INTO categories (name, description, user_id, category_id)
    VALUES (#{name}, #{description}, #{userId}, #{categoryId})
    RETURNING *
    """)
    @Results(id = "expenseMapping", value = {
            @Result()
    })
    Expense saveExpense(@Param("expense") ExpenseRequest expenseRequest);
}
