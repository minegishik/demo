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
	public String login (Model model) {
		
		 LoginUser loginUser = (LoginUser) loginService.getLoginUser();
		
		 model.addAttribute("loginUser", loginUser);
		 
		 System.out.println(loginUser);
		return "attendance/regist";
	}

}
