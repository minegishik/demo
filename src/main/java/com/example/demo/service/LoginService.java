package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.LoginUser;
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
	public LoginUser getLoginUser(Integer userId) {
		
		return loginMapper.getLoginUser(userId);
	}

}
