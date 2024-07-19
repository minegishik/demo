package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UsersDto;
import com.example.demo.entity.Users;
import com.example.demo.form.UserForm;
import com.example.demo.mapper.UserMapper;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserForm userForm;

	@Autowired
	private UsersDto usersDto;

	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;

	}

	public List<UsersDto> getAllUser() {
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

	public String inputCheck(UserForm userForm) {
		////
		if (userForm.getName() == null || userForm.getName().isEmpty() ||
				userForm.getPassword() == null || userForm.getPassword().isEmpty() ||
				userForm.getRole() == null || userForm.getRole().isEmpty() ||
				userForm.getStartDate() == null) {

			String errorMessage = "全ての項目を入力してください。";
			return errorMessage;
		}
		return "";
	}

	public String setUser(UserForm userForm) {
		// 既存ユーザーか新規ユーザーか判別する
		Users existingUser = userMapper.findUserDataByUserName(userForm.getName());

		Users newUser = new Users();

		if (existingUser == null) {
			//	新規ユーザー登録
			newUser = new Users();
			newUser.setName(usersDto.getName());
			newUser.setPassword(usersDto.getPassword());
			newUser.setRole(usersDto.getRole());
			newUser.setStartDate(usersDto.getStartDate());
			userMapper.insertUserData(newUser);

			String registerMessage = "登録が完了しました。";
			return registerMessage;

		} else {
			// ユーザー情報更新
			existingUser.setName(usersDto.getName());
			existingUser.setPassword(usersDto.getPassword());
			existingUser.setRole(usersDto.getRole());
			existingUser.setStartDate(usersDto.getStartDate());
			userMapper.updateUserData(existingUser);

			String updateMessage = "更新しました。";
			return updateMessage;
		}

	}

	//	public Boolean insertUserData(Users users) {
	//		return userMapper.insertUserData(users);
	//	}
	//
	//	public Boolean updateUserData(Users users) {
	//		return userMapper.updateUserData(users);
	//	}
}
