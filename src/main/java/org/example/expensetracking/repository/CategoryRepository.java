package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;


import java.util.List;

@Mapper
public interface CategoryRepository {
    //Get All Category
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
    List<Category> getAllCategory(Integer page, Integer size);

    //Post Category
    @Select("""
    INSERT INTO categories ( name, description, user_id ) VALUES
    VALUES (#{name}, #{description},#{userId})
    RETURNING *
""")
    @ResultMap("categoryMapping")
    List<Category> insertCategory(CategoryRequest categoryRequest);
}


