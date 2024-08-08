package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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

	/**
	 * 
	 *  登録・更新の処理
	 * @param userForm
	 * @param user
	 * @return
	 */
	public String settingUser(UserForm userForm, Users user) {
		// 既存ユーザーか新規ユーザーか判別する
		Users existingUser = userMapper.findUserDataByUserName(userForm.getName());

		Users newUser = new Users();

		// 新規ユーザーだった場合
		if (existingUser == null) {
			//	新規ユーザー登録
			newUser = new Users();
			newUser.setName(userForm.getName());
			newUser.setPassword(userForm.getPassword());
			newUser.setRole(userForm.getRole());
			newUser.setStartDate(userForm.getStartDate());
			userMapper.insertUserData(userForm.getName(), userForm.getUserId(), userForm.getPassword(),
					userForm.getRole(), userForm.getStartDate());

			return userForm.getName() + "を登録しました。";
		} else if (userForm.getPassword() != existingUser.getPassword() || userForm.getRole() != existingUser.getRole()
				|| userForm.getStartDate() != existingUser.getStartDate()) {

			// ユーザー情報更新
			existingUser.setName(userForm.getName());
			existingUser.setPassword(userForm.getPassword());
			existingUser.setRole(userForm.getRole());
			existingUser.setStartDate(userForm.getStartDate());
			userMapper.updateUserData(userForm.getName(), userForm.getUserId(), userForm.getPassword(),
					userForm.getRole(), userForm.getStartDate());

			return userForm.getName() + "の情報を更新しました。";

		}
		return "";
	}

	/**
	 * 
	 *  検索時の入力チェック項目
	 * @param userForm
	 * @param result
	 * @return
	 */
	public String validateSearch(UserForm userForm, BindingResult result) {
		if (userForm.getName() == null || userForm.getName().isEmpty()) {
			result.addError(new FieldError("name", "name", "※ユーザー名: 入力してください。"));
		} else if (userForm.getName() != null && 20 < userForm.getName().length()) {
			result.addError(new FieldError("name", "name", "※ユーザー名: 20文字以内で入力してください。"));
		} else if (userForm.getName() != null && userForm.getName().matches("[\\x20-\\x7E]+")) {
			result.rejectValue("name", "name3", "※ユーザー名: 全角で記入してください。");
		}
		return "";
	}

	/**
	 * 
	 *  登録時の入力チェック項目
	 * @param userForm
	 * @param result
	 * @return
	 */
	public String validateRegist(UserForm userForm, BindingResult result) {
		// ユーザー名がNullだった場合
		if (userForm.getName() == null || userForm.getName().isEmpty()) {
			result.rejectValue("name", "name1", "※ユーザー名: 入力してください。");
		}

		// ユーザー名が不正(文字数、全角の制限)だった場合
		if (userForm.getName() != null && 20 < userForm.getName().length()) {
			result.rejectValue("name", "name2", "※ユーザー名: 20文字以内で入力してください。");
		}

		if (userForm.getName() != null && userForm.getName().matches("[\\x20-\\x7E]+")) {
			result.rejectValue("name", "name3", "※ユーザー名: 全角で記入してください。");
		}

		// パスワードがNullだった場合
		if (userForm.getPassword() == null || userForm.getPassword().isEmpty()) {
			result.rejectValue("password", "password", "※パスワード: 入力してください。");
		}

		// パスワードが不正(17桁以上)だった場合
		if (userForm.getPassword() != null && 16 < userForm.getPassword().length()) {
			result.rejectValue("password", "password", "※パスワード: 桁数は16桁以下で入力してください。");
		}

		// 権限が"未選択"だった場合
		if (userForm.getRole() == null || userForm.getRole().equals("select")) {
			result.rejectValue("role", "role", "※権限: 選択してください。");
		}
		// 利用開始日の形式が間違いだった場合
		if (!isValidDate(userForm.getStartDate())) {
			result.rejectValue("startDate", "startDate", "※利用開始日: 例:yyyy-mm-dd の形式で入力してください。");
		}
		return "";

	}

	/**
	 * 
	 *  ユーザーの削除条件
	 * @param userForm
	 * @param user
	 * @return
	 */
	public String deleteUser(UserForm userForm, Users user) {
		if (user != null && "9999-99-99".equals(userForm.getStartDate())) {
			userMapper.deleteUserData(user.getUserId());

		}
		return "";

	}

	/**
	 * 
	 *  
	 * @param dateStr
	 * @return
	 */
	private boolean isValidDate(String dateStr) {
		if (dateStr.equals("9999-99-99")) {
			return true; // 特定の日付"9999-99-99"を有効として扱う
		}
		try {
			LocalDate parsedDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			return true;
		} catch (DateTimeParseException e) {
			return false;
		}

	}
}
