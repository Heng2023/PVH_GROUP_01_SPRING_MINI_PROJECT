package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.RegisterRequest;

@Mapper
public interface UserRepository {
    @Select("""
            INSERT INTO users (user_id,email,password,profile_image) VALUES (DEFAULT,#{user.email} , #{user.password},#{user.profileImage})
            """)
    User createUser(@Param("user") RegisterRequest registerRequest);

    @Select("""
        SELECT * FROM users WHERE email = #{email}
    """)
    User findUserByEmail(String email);
}
