package com.example.demo.controller;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.LoginUser;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;
import com.example.demo.util.LoginUserUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private LoginUserUtil loginUserUtil;
	
	
	
	/**
	 * ログイン画面 初期表示
	 * @return ログイン画面
	 */
	@RequestMapping("")
	public String index(@ModelAttribute LoginForm loginForm) {
		
		
		return "login/index";
	}
	
	
	/**
	 * ログイン画面 『ログイン』ボタン押下
	 * 
	 * @return 勤怠登録画面
	 */
	@PostMapping("/login")
	public String login(@ModelAttribute LoginForm loginForm, Model model, HttpSession session) {
		
		LoginUser loginUser = loginService.getLoginUser(loginForm.getUserId());
		
		// ユーザーIDが存在し、パスワードが一致するか確認
	    if (loginUser != null && loginUser.getPassword().equals(loginForm.getPassword())) {
	        // 利用開始日チェック
	        LocalDate today = LocalDate.now(); // 現在の日付を取得

	        // Date 型から LocalDate 型に変換
	        LocalDate startDate = loginUser.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	        if (startDate != null && startDate.isAfter(today)) {
	            model.addAttribute("loginError", "※利用開始日までログインできません。");
	            return "/login/index";
	        }

	        // ログイン成功時の処理
	        session.setAttribute("loginUser", loginUser); // ログインユーザー情報をセッションに保存

	        // ログイン後の初期画面にリダイレクト
	        return loginUserUtil.sendDisp();
	    } else {
	        model.addAttribute("loginError", "ユーザーID、パスワードが不正、もしくはユーザーが無効です。");
	        return "/login/index";
	    }
	}

}
