package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.UsersDto;
import com.example.demo.entity.Users;

@Mapper
public interface UserMapper {

	List<UsersDto> getAllUser();
	
	

	/**
	 * ユーザー情報取得（ユーザー名）
	 * 
	 * @Param name
	 * 
	 */
	Users findUserDataByUserName(@Param("name") String name);

	Boolean updateUserData(@Param("users") Users users);

	Boolean insertUserData(@Param("users") Users users);

}
// Users users


//
//@Param("name") String name, @Param("password") String password,
//@Param("role") String role, @Param("startDate") Date startDate?