package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.configuration.UUIDTypeHandler;
import org.example.expensetracking.model.Category;


import java.util.List;
import java.util.UUID;

@Mapper
public interface CategoryRepository {
    @Select("""
       SELECT * FROM categories WHERE user_id = #{userId}
       LIMIT #{size}
       OFFSET #{size} * (#{page} - 1)
       """)
    @Results(id = "categoryMapping", value = {
            @Result(property = "categoryID",column = "category_id",typeHandler = UUIDTypeHandler.class),
            @Result(property = "user",column = "user_id",
            one = @One(select = "org.example.expensetracking.repository.UserRepository.findUserById"))
    })
    List<Category> getAllCategories(UUID userId, Integer page, Integer size);
}
