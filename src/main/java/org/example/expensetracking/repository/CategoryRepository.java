package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.example.expensetracking.model.Category;


import java.util.List;

@Mapper
public interface CategoryRepository {
    @Select("""
              SELECT * FROM categories
       LIMIT #{size}
       OFFSET #{size} * (#{page} - 1)
       """)
    @Results(id = "categoryMapping", value = {
            @Result(property = "categoryID",column = "category_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "userId",column = "user_id")
    })
    List<Category> getAllCategories(Integer page, Integer size);
}
