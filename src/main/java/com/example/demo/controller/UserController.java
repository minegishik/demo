package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.UsersDto;
import com.example.demo.entity.Users;
import com.example.demo.form.UserForm;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * ユーザー管理画面 初期表示
	 * @return
	 */

	@GetMapping("/management")
	public String showUserManagement() {
		return "user/management";
	}

	/**
	 * 
	 *ユーザー検索結果表示
	 * @param name
	 * @param model
	 * @return
	 */

	@PostMapping(value = "/management", params = "search")
	public String showUserData(@ModelAttribute UserForm userForm, Model model) {

		List<UsersDto> getAllUser = userService.getAllUser();

		for (UsersDto user : getAllUser) {

			if (user.getName().equals(userForm.getName())) {
				model.addAttribute("user", user);
			}

		}

		//		// ユーザー名を取得
		//		String name = userForm.getName();
		//		// ユーザーデータを取得
		//		Users userData = userService.getUserDataByUserName(name);
		//		
		//		// 既に登録されたユーザーの場合
		//		if(userData != null) {
		//			// モデルにユーザーデータを追加
		//			model.addAttribute("userId", userData.getUserId());
		//			model.addAttribute("password", userData.getPassword());
		//			model.addAttribute("role", userData.getRole());
		//			model.addAttribute("startDate", userData.getStartDate());

		return "user/management";
		//			
		//		} else {
		//			// 登録されていないユーザーの場合
		////			model.addAttribute("showMessage","ユーザーを新規作成する場合、下記項目を入力");
		//			
		//			// ランダムにユーザーIDを生成
		//			Random rand = new Random();
		//				UserForm userForm2 = new UserForm();
		//			    Integer randomNumber = rand.nextInt(2147483647);
		//			    userForm2.setUserId(randomNumber);
		//			    
		//			 // 生成したIDを表示させる
		//			    model.addAttribute("userId", userForm2.getUserId());
		//			    return "user/management";
		//			 
		//			 
		//		}

	}

	/**
	 * 
	 * 新規ユーザー登録
	 * @param users
	 * @param model
	 * @return
	 */

	@PostMapping(value = "/management", params = "regist")
	public String registUser(@ModelAttribute Users users, Model model) {

		userService.insertUserData(users);
		model.addAttribute("userData", users);

		return "user/management";
	}

	/**
	 * 
	 * 新規ユーザー登録完了
	 * @return
	 */

	//	@PostMapping(value = "/management")
	//	public String completeUserRegist() {
	//		return "user/management";
	//	}

}
