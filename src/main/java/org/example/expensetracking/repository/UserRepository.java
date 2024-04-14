package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.AppUserDTO;

@Mapper
public interface UserRepository {
    @Results(id = "AppUserMapping", value = {
            @Result(property = "profileImage", column = "profile_image")
    })
    @Select("""        
        INSERT INTO users  VALUES (DEFAULT,#{user.email} , #{user.password},#{user.profileImage}) RETURNING *
    """)
    AppUserDTO saveUser(@Param("user") RegisterRequest registerRequest);

    @Select("""
        SELECT * FROM users WHERE email = #{email}
    """)
    User findUserByEmail(@Param("email") String email);
}
