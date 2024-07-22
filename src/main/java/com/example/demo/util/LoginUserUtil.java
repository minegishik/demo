package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.LoginUser;

import jakarta.servlet.http.HttpSession;

@Component
public class LoginUserUtil {
	
	 @Autowired
	    private HttpSession session;

	    public LoginUser getLoginUser() {
	        return (LoginUser) session.getAttribute("loginUser");
	    }

	/**
	 * ログインしているユーザーがシステム管理者か判定
	 * 
	 * @return 管理者の場合　true
	 *  */
	public boolean isAdmin() {
		LoginUser loginUser = getLoginUser();
		String userRole = loginUser.getRole();
        return Constants.CODE_VAL_ROLL_ADMIN.equals(userRole);
	}

	/**
	 * ログインしているユーザーがユニットマネージャーか判定
	 * 
	 * @return ユニットマネージャーの場合　true
	 *  */
	public boolean isUnitManager() {
		LoginUser loginUser = getLoginUser();
		String userRole = loginUser.getRole();
		return Constants.CODE_VAL_ROLL_UNITMANAGER.equals(userRole);
	}

	/**
	 * ログインしているユーザーがマネージャーか判定
	 * 
	 * @return マネージャーの場合　true
	 *  */
	public boolean isManager() {
		LoginUser loginUser = getLoginUser();
		String userRole = loginUser.getRole();
		return Constants.CODE_VAL_ROLL_MANAGER.equals(userRole);
	}

	/**
	 * ログインしているユーザーが社員か判定
	 * 
	 * @return 社員の場合　true
	 *  */
	public boolean isRegular() {
		LoginUser loginUser = getLoginUser();
		String userRole = loginUser.getRole();
		return Constants.CODE_VAL_ROLL_REGULAR.equals(userRole);
	}
	
	/**
	 * 遷移先画面の決定
	 * 
	 * @return 権限ごとの遷移先
	 */
	public String sendDisp() {
		
		if (isAdmin()) {
		    return "redirect:/user/management"; // システム管理者はユーザー管理画面にリダイレクト
		} else if (isUnitManager()) {
		    return "redirect:/attendance/regist"; // ユニットマネージャーは勤怠登録画面にリダイレクト
		} else if (isManager()) {
		    return "redirect:/attendance/regist"; // マネージャーは勤怠登録画面にリダイレクト
		} else if (isRegular()) {
		    return "redirect:/attendance/regist"; // 社員は勤怠登録画面にリダイレクト
		} else {
		    return "redirect:/login/index"; // その他の権限があればログイン画面にリダイレクト
		}
	}
	
}
