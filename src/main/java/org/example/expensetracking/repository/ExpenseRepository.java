package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.configuration.UUIDTypeHandler;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;

import java.util.List;
import java.util.UUID;


@Mapper
public interface ExpenseRepository {

    @Select("""
    INSERT INTO expenses (amount, description, date, user_id, category_id)
    VALUES (#{expense.amount}, #{expense.description},#{expense.date}, #{userId}, #{categoryId})
    RETURNING *
    """)
    @ResultMap("expanseMapping")
    Expense saveExpense(@Param("expense") ExpenseRequest expenseRequest, UUID userId);


    @Select("""
    SELECT * FROM expenses
    """)
    @Results(id = "expanseMapping", value = {
            @Result(property = "expenseId", column = "expense_id"),
            @Result(property = "user", column = "user_id",
                    one = @One(select = "org.example.expensetracking.repository.UserRepository.findUserById")
            ),
            @Result(property = "category", column = "category_id",
                    one = @One(select = "org.example.expensetracking.repository.CategoryRepository.findCategoryById")
            )
    })
    List<Expense> findAllExpense();
}
