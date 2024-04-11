package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.example.expensetracking.model.User;

import java.util.List;

@Mapper
public interface UserRepository {
    @Select("""
              SELECT * FROM users
       LIMIT #{size}
       OFFSET #{size} * (#{page} - 1)
       """)
    @Results (id = "userMapping", value = {
            @Result(property = "userId",column = "user_id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "profileImage",column = "profile_image")
    })
    List<User> getAllUsers(Integer page, Integer size);
}
