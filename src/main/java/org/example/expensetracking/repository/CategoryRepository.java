package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.configuration.UUIDTypeHandler;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;


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


    @Select("""
    SELECT * FROM categories WHERE category_id = #{categoryId}
    AND user_id = #{userId}
    """)
   Category getCategoryById(UUID categoryId,UUID userId);

    @Select("""
    INSERT INTO categories (name, description, user_id)
    VALUES ( #{category.name}, #{category.description}, #{userId})
    """)
    @ResultMap("categoryMapping")
    Category insertCategory(@Param("category") CategoryRequest categoryRequest, UUID userId);

    @Select("""
    UPDATE categories SET name = #{category.name}, description = #{category.description}
    WHERE category_id = #{userId}
    """)
    @ResultMap("categoryMapping")
    Category UpdateCategory(@Param("category") CategoryRequest categoryRequest, UUID userId);

    @Delete("""
    DELETE FROM categories
    WHERE category_id = #{id}
    """)
    void deleteCategory(UUID Id);

    @Select("""
    SELECT * FROM categories
    WHERE category_id = #{categoryId}
    """)
    Category findCategoryById(UUID categoryId);
}
