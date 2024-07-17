package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginUserDto;
import com.example.demo.mapper.LoginMapper;

@Service
public class LoginService {
	
	@Autowired
	private LoginMapper loginMapper;
	
	
	/**
	 * ログイン処理
	 * 
	 * @param userId
	 * @param password
	 * @return String
	 */
	public List<LoginUserDto> getLoginUser(Integer userId, String password) {
		
		List<LoginUserDto> LoginUserDtoList = loginMapper.getLoginUser(userId, password);
		
		System.out.println(LoginUserDtoList);
		return LoginUserDtoList;
	}

}
