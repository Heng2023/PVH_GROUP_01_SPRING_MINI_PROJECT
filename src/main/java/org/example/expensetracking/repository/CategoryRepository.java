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
    INSERT INTO categories (name, description , user_id)
    VALUES (#{category.name}, #{category.description}, #{user.id})
    """)
    Category insertCategory(@Param("category") CategoryRequest categoryRequest);

    @Select("""
            SELECT * FROM categories WHERE user_id = #{user.id}
            """)
    @Results(id = "categoryMap",value = {
            @Result(property = "categoryID",column = "category_id",typeHandler = UUIDTypeHandler.class),
            @Result(property = "user",column = "user_id",
            one = @One(select = "org.example.expensetracking.repository.UserRepository.findUserById")
            )
    })
    List<Category> findAll(UUID userId);


    @Select("""
    SELECT * FROM categories WHERE id = #{id}
    """)
    Category findById(Long id);

    @Select("""
    UPDATE categories SET name = #{name} WHERE id = #{id}
    """)
    Category updateCategory(Integer id, CategoryRequest categoryRequest);

    @Delete("""
    DELETE FROM categories WHERE id = #{id}
    """)
    void deleteCategory(Integer id);
}
