package com.example.demo.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.UsersDto;
import com.example.demo.entity.LoginUser;
import com.example.demo.entity.Users;
import com.example.demo.form.LoginForm;
import com.example.demo.form.UserForm;
import com.example.demo.service.LoginService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final LoginService loginService;

	@Autowired
	public UserController(UserService userService, LoginService loginService) {
		this.userService = userService;
		this.loginService = loginService;

	}

	/**
	 * ユーザー管理画面 初期表示
	 * @return
	 */

	@GetMapping("/management")
	public String showUserManagement(HttpSession session, Model model) {
		UserForm userForm = new UserForm();
		String searchedName = (String) session.getAttribute("searchedName");
		if (searchedName != null) {
			userForm.setName(searchedName);
			session.removeAttribute("searchedName"); // 不要になったら削除する
		}
		model.addAttribute("userForm", new UserForm());
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
	public String showUserData(@ModelAttribute UserForm userForm, BindingResult result,
			@ModelAttribute LoginForm loginForm, Model model,
			HttpSession session) {

		userService.validateSearch(userForm, result);

		if (result.hasErrors()) {
			// エラーがある場合は、エラーを表示して管理画面に戻る
			model.addAttribute("errors", result.getAllErrors());
			return "user/management";
		}

		List<UsersDto> getAllUser = userService.getAllUser();
		LoginUser loginUser = loginService.getLoginUser(loginForm.getUserId()); // 検索条件に合致するユーザーを探す
		UsersDto foundUser = null;
		for (UsersDto user : getAllUser) {
			if (user.getName().equals(userForm.getName())) {
				foundUser = user;
				break; // ユーザーが見つかったらループを終了させる  
			}
		}
		if (foundUser != null) {

			model.addAttribute("user", foundUser);
		} else {
			// 新規ユーザーの場合、ランダムな整数を生成してuserIDに設定
			Random rand = new Random();
			Integer randomNumber = rand.nextInt(2147483647);
			// UserFormにランダムな整数を設定
			UserForm newUserForm = new UserForm();
			newUserForm.setUserId(randomNumber);

			model.addAttribute("userForm", userForm);
			model.addAttribute("user", newUserForm);
		}
		// 入力したユーザー名を保持する
		session.setAttribute("searchedName", userForm.getName());
		session.setAttribute("loginUser", loginUser);

		return "user/management";
	}

	/**
	 * 
	 * 新規ユーザー登録・更新・削除
	 * @param users
	 * @param model
	 * @return
	 */

	@PostMapping(value = "/management", params = "regist")
	public String registerUser(@ModelAttribute UserForm userForm, Model model, BindingResult result, Users user,
			RedirectAttributes redirectAttributes) {

		userService.validateRegist(userForm, result);

		if (result.hasErrors()) {
			// エラーがある場合は、エラーを表示して管理画面に戻る
			model.addAttribute("errors", result.getAllErrors());
			return "user/management";

		}

		String message = "";
		if ("9999-99-99".equals(userForm.getStartDate().toString())) {
			// 削除時の処理
			userService.deleteUser(userForm, user);
			model.addAttribute("deleteMessage", userForm.getName() + "を削除しました。");
			return "user/management";

		} else {

			message = userService.settingUser(userForm, user);
		}

		if (message.contains("登録")) {
			// 登録時の処理
			userService.settingUser(userForm, user);
			model.addAttribute("registMessage", message);

		} else if (message.contains("更新")) {

			userService.settingUser(userForm, user);
			model.addAttribute("updateMessage", message);
		}

		return "user/management";

	}

}
