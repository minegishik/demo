package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.AttendanceDto;
import com.example.demo.entity.LoginUser;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {
	
	@Autowired 
	LoginService loginService;
	@Autowired
	AttendanceService attendanceService;
	
	/**
	 * 勤怠登録画面 初期表示
	 * 
	 * 
	 * @return　勤怠登録画面
	 */
	@RequestMapping("/regist")
	public String regist(HttpSession session, Model model) {

		LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		
		if (loginUser != null) {
            // ログインユーザーのIDを取得
            Integer userId = loginUser.getUserId();

            // 勤怠情報を取得
            List<AttendanceDto> attendanceDtoList = attendanceService.getAttendanceInfo(userId);
            model.addAttribute("attendanceDtoList", attendanceDtoList);
        }
		
		System.out.println(model);

		return "attendance/regist";
	}
	
	/**
	 * 勤怠登録画面 『登録』ボタン押下
	 * 
	 * @param model
	 * @return 勤怠登録画面
	 */
	@PostMapping(path="/regist", params="punchIn")
	public String punchIn() {
		
		return "attendance/regist";
	}
	
	/**
	 * 勤怠登録画面 『表示』ボタン押下
	 * 
	 * @param model
	 * @return 勤怠登録画面
	 */
	@PostMapping(path="/regist", params="display")
	public String displayIn() {
		
		return "attendance/regist";
	}

}
