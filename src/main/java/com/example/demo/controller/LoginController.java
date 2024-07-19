package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.LoginUser;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	/**
	 * ログイン画面 初期表示
	 * 
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
		
		 
		 if (loginUser != null && loginUser.getPassword().equals(loginForm.getPassword())) {
		        // ログイン成功時の処理
			 session.setAttribute("loginUser", loginUser);
		        return "redirect:/attendance/regist";  // ログイン後の画面に遷移
		    } else {
		    	model.addAttribute("error", "ユーザIDまたはパスワードが正しくありません。");
		        return "login/index";  // ログイン画面に戻るなど、エラー処理
		    }
		 
		
	}
	
	

}
