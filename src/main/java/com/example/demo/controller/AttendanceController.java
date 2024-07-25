package com.example.demo.controller;
 

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.AttendanceUser;
import com.example.demo.entity.LoginUser;
import com.example.demo.form.AttendanceForm;
import com.example.demo.form.AttendanceFormList;
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
		AttendanceFormList attendanceFormList = new AttendanceFormList();
		model.addAttribute("attendanceFormList",attendanceFormList);
	 

		return "attendance/regist";
	}
	
	/**
	 * 勤怠登録画面 『表示』ボタン押下
	 * 
	 * @param model
	 * @return 勤怠登録画面
	 */
	@PostMapping(path="/regist", params="display")
	public String displayIn(Integer year, Integer month, @ModelAttribute AttendanceFormList formList,HttpSession session, Model model) {
		
		LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		int userId = loginUser.getUserId();
		model.addAttribute("selectedYear", year);
		model.addAttribute("selectedMonth", month);
		
	    List<AttendanceUser> calendar = attendanceService.getCalendar(year, month);
	    model.addAttribute("calendar", calendar);
	    
	    List<AttendanceUser> attendanceList = attendanceService.getAttendanceYearMonth(userId,year,month);
	    model.addAttribute("attendanceList", attendanceList);
	   
	
	    //比較
	    for (AttendanceUser day : calendar) {
	    	boolean found = false;
			for (AttendanceUser attendance : attendanceList) {
				if (day.getDate().equals(attendance.getDate())) {
						day.setAttendanceId(attendance.getAttendanceId());
						day.setUserId(attendance.getUserId());
						day.setStatus(attendance.getStatus());
						day.setStartTime(attendance.getStartTime());
						day.setEndTime(attendance.getEndTime());
						day.setRemarks(attendance.getRemarks());
						
						
				}
			}
			
			if (!found) {
		        day.setDate(day.getDate());
		    }
		}
	    
	    //フォームに詰替え
	    List<AttendanceForm> form = new ArrayList<AttendanceForm>();
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d");
	    DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("E");
	    
	    for (AttendanceUser day2 : calendar) {
		    	AttendanceForm attendanceForm = new AttendanceForm();
		    	attendanceForm.setUserId(day2.getUserId());
				attendanceForm.setUserId(loginUser.getUserId());
				attendanceForm.setStatus(day2.getStatus());
				attendanceForm.setDate(day2.getDate());
				attendanceForm.setStartTime(day2.getStartTime());
				attendanceForm.setEndTime(day2.getEndTime());
				attendanceForm.setRemarks(day2.getRemarks());
				
				// Date型からLocalDate型に変換する
				LocalDate localDate = day2.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				// 日付と曜日のフォーマットをセット
				// 日付と曜日のフォーマットをセット
			    String formattedDate = localDate.format(dateFormatter);
			    attendanceForm.setFormattedDate(formattedDate);
			    String formattedDayOfWeek = localDate.format(dayOfWeekFormatter);
			    attendanceForm.setFormattedWeek(formattedDayOfWeek);
			    
			    form.add(attendanceForm); // フォームリストに追加する
	    }
	    
	    formList.setAttendanceFormList(form);
		model.addAttribute("formList", formList);
		
		System.out.println(formList);
		
	    
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
	
 
}