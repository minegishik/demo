package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.example.demo.dto.LoginUserDto;

@Component
@Mapper
public interface LoginMapper {
	
	/**
	 * ログイン情報取得
	 * 
	 * @param userId
	 * @param password
	 * 
	 * @return ログイン情報エンティティ
	 */
	List<LoginUserDto> getLoginUser(@Param("userId") Integer userId, @Param("password") String password);

}
