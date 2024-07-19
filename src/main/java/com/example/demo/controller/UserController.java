package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.UsersDto;
import com.example.demo.form.UserForm;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

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
		return "user/management";

	}

	/**
	 * 
	 * 新規ユーザー登録・既存ユーザ情報更新
	 * @param users
	 * @param model
	 * @return
	 */

	@PostMapping(value = "/management", params = "regist")
	public String registerUser(@ModelAttribute UserForm useForm, Model model) {

		// 未入力エラーメッセージ
		String errorMessage = userService.inputCheck(useForm);
		model.addAttribute("eroorMessage", errorMessage);

		if (!errorMessage.isEmpty()) {

			// 登録完了メッセージ
			String registerMessage = userService.inputCheck(useForm);
			model.addAttribute("registerMessage", registerMessage);

		} else {
			// 更新完了メッセージ
			String updateMessage = userService.inputCheck(useForm);
			model.addAttribute("updateMessage", updateMessage);

		}
		// 登録後にリダイレクトして再読み込みで重複登録を防ぐ
		return "redirect:/user/management";

	}
}

//		userService.insertUserData(users);
//		model.addAttribute("userData", users);
//
//		return "user/management";
/**
 * 
 * 新規ユーザー登録完了
 * @return
 */

//	@PostMapping(value = "/management")
//	public String completeUserRegist() {
//		return "user/management";
//	}
