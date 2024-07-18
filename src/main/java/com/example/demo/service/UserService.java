package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.UsersDto;
import com.example.demo.entity.Users;
import com.example.demo.mapper.UserMapper;

@Service
public class UserService {

	private final UserMapper userMapper;

	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;

	}
	
	public List<UsersDto> getAllUser(){
		return userMapper.getAllUser();
	}
	
	
	/**
	 * 
	 * ユーザー情報取得
	 * @param name
	 * @return
	 */

	public Users getUserDataByUserName(String name) {
		Users userData = userMapper.findUserDataByUserName(name);
		return userData;
	}
	
	/**
	 * 
	 * ユーザー情報更新
	 * @param users
	 * @return
	 */

	public Boolean updateUserData(Users users) {
		return userMapper.updateUserData(users);
	}
	
	/**
	 * 
	 * 新規ユーザー情報登録
	 * @param users
	 * @return
	 */

	public Boolean insertUserData(Users users) {
		return userMapper.insertUserData(users);
	}

}
