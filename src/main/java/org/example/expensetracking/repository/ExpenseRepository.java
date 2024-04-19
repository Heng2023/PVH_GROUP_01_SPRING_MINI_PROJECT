package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.configuration.UUIDTypeHandler;
import org.example.expensetracking.model.Expense;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ExpenseRepository {
    @Select("""
        INSERT 
        INTO expenses (amount, description, date, user_id, category_id) 
        VALUES (#{amount}, #{description}, #{date}, #{user.userId}, #{category.categoryId})
        RETURNING *
    """)
    @Results(id = "expenseMapping", value = {
            @Result(property = "expenseId", column = "expense_id", typeHandler = UUIDTypeHandler.class),
            @Result(property = "category",column = "category_id",
                    one = @One(select = "org.example.expensetracking.repository.CategoryRepository.getCategoryById")),
            @Result(property = "user",column = "user_id",
                    one = @One(select = "org.example.expensetracking.repository.UserRepository.findUserById"))
    })
    Expense save(Expense expense);

    @Select("""
        SELECT e.*, c.*
        FROM expenses e
        LEFT JOIN categories c ON e.category_id = c.category_id
    """)
    @Results({
            @Result(property = "expenseId", column = "expense_id", typeHandler = UUIDTypeHandler.class),
            @Result(property = "user",column = "user_id",
                    one = @One(select = "org.example.expensetracking.repository.UserRepository.findUserById")),
            @Result(property = "category",column = "category_id",
                    one = @One(select = "org.example.expensetracking.repository.CategoryRepository.getCategoryById"))
    })
    List<Expense> findAllExpenses();

    @Delete("""
        DELETE FROM expenses WHERE expense_id = #{expenseId}
    """)
    void deleteExpenseById(UUID expenseId);


    @Select("""
    SELECT *
        FROM expenses
        WHERE expense_id = #{expenseId}
    """)
    @ResultMap("expenseMapping")
    Expense getExpenseById(UUID expenseId);

    @Select("""
    UPDATE expenses
        SET amount = #{amount}, description = #{description}, date = #{date}, category_id = #{category.categoryId}
        WHERE expense_id = #{expenseId}
        RETURNING *
    """)
    @ResultMap("expenseMapping")
    Expense updateExpense(Expense expense);

}
