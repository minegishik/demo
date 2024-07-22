package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.LoginUserDto;

@Component
public class LoginUserUtil {
	
	@Autowired
	private LoginUserDto loginUserDto;
	
	
	/**
	 * ログイン情報を取得
	 * @return
	 */
	public LoginUserDto getLoginUserDto() {
		
		return loginUserDto;
	}

	/**
	 * ログインしているユーザーがシステム管理者か判定
	 * 
	 * @return 管理者の場合　true
	 *  */
	public boolean isAdmin() {
		return Constants.CODE_VAL_ROLL_ADMIN.equals(getLoginUserDto().getRole());
	}

	/**
	 * ログインしているユーザーがユニットマネージャーか判定
	 * 
	 * @return ユニットマネージャーの場合　true
	 *  */
	public boolean isUnitManager() {
		return Constants.CODE_VAL_ROLL_UNITMANAGER.equals(getLoginUserDto().getRole());
	}

	/**
	 * ログインしているユーザーがマネージャーか判定
	 * 
	 * @return マネージャーの場合　true
	 *  */
	public boolean isManager() {
		return Constants.CODE_VAL_ROLL_MANAGER.equals(getLoginUserDto().getRole());
	}

	/**
	 * ログインしているユーザーが社員か判定
	 * 
	 * @return 社員の場合　true
	 *  */
	public boolean isRegular() {
		return Constants.CODE_VAL_ROLL_REGULAR.equals(getLoginUserDto().getRole());
	}
	
}
