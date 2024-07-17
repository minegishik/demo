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
	 * 
	 * @return ログイン情報エンティティ
	 */
	LoginUser getLoginUser(@Param("userId") Integer userId);

}
