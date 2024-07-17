package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {
	
	/**
	 * 勤怠登録画面 初期表示
	 * 
	 * 
	 * @return　勤怠登録画面
	 */
	@RequestMapping("/regist")
	public String regist() {
		
		return "attendance/regist";
	}

}
