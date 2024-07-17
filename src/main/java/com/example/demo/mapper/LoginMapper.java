package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.LoginUser;


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
	LoginUser getLoginUser(@Param("userId") Integer userId, @Param("password") String password);

}
