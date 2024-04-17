package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.configuration.UUIDTypeHandler;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.AppUserDTO;

@Mapper
public interface UserRepository {
    @Results(id = "UserMapping", value = {
            @Result(property = "userId", column = "user_id",typeHandler = UUIDTypeHandler.class), // Map userId column to userId property
            @Result(property = "profileImage", column = "profile_image")
    })
    @Select("""
        INSERT INTO users  VALUES (DEFAULT,#{user.email} , #{user.password},#{user.profileImage}) RETURNING user_id, *
    """)
    AppUserDTO saveUser(@Param("user") RegisterRequest registerRequest);

    @Select("""
        SELECT * FROM users WHERE email = #{email}
    """)
    User findUserByEmail(@Param("email") String email);

    @Update("UPDATE users SET password = #{encodedPassword} WHERE email = #{email}")
    AppUserDTO updatePasswordByEmail(@Param("email") String email, @Param("encodedPassword") String encodedPassword);

    @Select("""
            SELECT * FROM user WHERE user_id = #{userId}
            """)
    @ResultMap("UserMapping")
    User findUserById(@Param("userId") Long userId);

}
