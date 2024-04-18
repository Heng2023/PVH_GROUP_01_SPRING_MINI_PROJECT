package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.Mapper;

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
    //Get Category BY ID
    @Select("""
    SELECT * FROM categories WHERE category_id = #{categoryId}
    AND user_id = #{userId}
    """)
    @ResultMap("categoryMapping")
   Category getCategoryById(UUID categoryId,UUID userId);
    //Delete Category
    @Select("""
    DELETE  FROM categories WHERE category_id = #{categoryId}
    AND user_id = #{userId}
    """)
    void deleteCategoryById (UUID categoryId,UUID userId);


}
