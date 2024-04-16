package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;

import java.util.List;

@Mapper
public interface CategoryRepository {

    @Select("""
    INSERT INTO categories (name, description , user_id)
    VALUES (#{category.name}, #{category.description}, #{user.id})
    """)
    Category insertCategory(@Param("category") CategoryRequest categoryRequest);

    @Select("""
    SELECT * FROM categories
    """)
    List<Category> findAll();


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
