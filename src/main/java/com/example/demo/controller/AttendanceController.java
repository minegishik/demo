package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		model.addAttribute("attendanceFormList", attendanceFormList);

		return "attendance/regist";
	}

	/**
	 * 勤怠登録画面 『表示』ボタン押下
	 * 
	 * @param model
	 * @return 勤怠登録画面
	 */
	@PostMapping(path = "/regist", params = "display")
	public String displayIn(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month,
			@ModelAttribute AttendanceFormList formList,
			HttpSession session, Model model) {

		LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		int userId = loginUser.getUserId();
		model.addAttribute("selectedYear", year);
		model.addAttribute("selectedMonth", month);
		session.setAttribute("selectedYear", year);
        session.setAttribute("selectedMonth", month);

		if (year == null || month == null) {
			model.addAttribute("errorMessage", "※年月を指定してください");
			return "attendance/regist"; // 同じ画面に戻る
		}
		
		// ステータス（承認状況）を表示
		String statusMessage = attendanceService.checkStatus(userId, year, month);
		model.addAttribute("statusMessage", statusMessage);

		List<AttendanceUser> calendar = attendanceService.getCalendar(year, month);
		model.addAttribute("calendar", calendar);

		List<AttendanceUser> attendanceList = attendanceService.getAttendanceYearMonth(userId, year, month);
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
			attendanceForm.setUserId(loginUser.getUserId());
			attendanceForm.setStatus(day2.getStatus());
			attendanceForm.setDate(day2.getDate());
			attendanceForm.setStartTime(day2.getStartTime());
			attendanceForm.setEndTime(day2.getEndTime());
			attendanceForm.setRemarks(day2.getRemarks());

			
			// 日付と曜日のフォーマットをセット
			// 日付と曜日のフォーマットをセット
			String formattedDate = day2.getDate().format(dateFormatter);
			attendanceForm.setFormattedDate(formattedDate);
			String formattedDayOfWeek = day2.getDate().format(dayOfWeekFormatter);
			attendanceForm.setFormattedWeek(formattedDayOfWeek);


			form.add(attendanceForm); // フォームリストに追加する

		}

		formList.setAttendanceFormList(form);
		model.addAttribute("formList", formList);
		
		

		return "attendance/regist";
	}

	/**
	 * 勤怠登録画面 『登録』ボタン押下
	 * 
	 * @param model
	 * @return 勤怠登録画面
	 */
	@PostMapping(path = "/regist", params = "insert")
	public String punchIn(@RequestParam(required = false) Integer year,
	        @RequestParam(required = false) Integer month,
	        @ModelAttribute AttendanceFormList formList,
	        BindingResult result,
	        Model model,
	        HttpSession session) {
		
		// セッションから選択された年月を取得
	    Integer selectedYear = (Integer) session.getAttribute("selectedYear");
	    Integer selectedMonth = (Integer) session.getAttribute("selectedMonth");
	    
	 // セッションから取得した年月をモデルに追加
	    model.addAttribute("selectedYear", selectedYear);
	    model.addAttribute("selectedMonth", selectedMonth);
        
		LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
	    model.addAttribute("loginUser", loginUser);
	    int userId = loginUser.getUserId();
	    
//	  //勤怠情報を削除
	    for(AttendanceForm attendanceForm : formList.getAttendanceFormList()) {
	    	attendanceService.deleteAttendance(userId, attendanceForm.getDate());
	    }
	    
	    
	    
	 // フォーマッターの設定
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d");
	    DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("E");
	    
	 // form リストの定義
	    List<AttendanceForm> form = new ArrayList<>();

//	    // 新しい勤怠情報の登録処理
		for (AttendanceForm attendanceForm : formList.getAttendanceFormList()) {
			if (attendanceForm.getStatus() == null) {
			    attendanceForm.setStatus(99); // デフォルトのステータス
			}
			if (attendanceForm.getDate() != null) {
	            String formattedDate = attendanceForm.getDate().format(dateFormatter);
	            System.out.println("Formatted Date: " + formattedDate);
	        }
			
			LocalDate date = attendanceForm.getDate();
			
			if(attendanceForm.getStatus() != null) {
				// 日付と曜日のフォーマットをセット
                String formattedDate = date.format(dateFormatter);
                attendanceForm.setFormattedDate(formattedDate);

                String formattedDayOfWeek = date.format(dayOfWeekFormatter);
                attendanceForm.setFormattedWeek(formattedDayOfWeek);
             // フォームリストに追加する
                form.add(attendanceForm);
			//エンティティにセット
			AttendanceUser newAttendance = new AttendanceUser();
			// form から AttendanceUser に必要な情報をセット
			newAttendance.setUserId(userId);
			newAttendance.setStatus(attendanceForm.getStatus());
		    newAttendance.setDate(attendanceForm.getDate());
			newAttendance.setStartTime(attendanceForm.getStartTime());
			newAttendance.setEndTime(attendanceForm.getEndTime());
			newAttendance.setRemarks(attendanceForm.getRemarks());
			
			
			
			attendanceService.insertAttendance(newAttendance);
			
			}
		}
		

		// 成功メッセージの設定
	    model.addAttribute("successMessage", "勤怠情報が正常に登録されました。");

	 
        return displayIn(selectedYear, selectedMonth, formList, session, model);
	}
	
	/**
	 * 勤怠登録画面 『承認申請』ボタン押下
	 * 
	 * @param userId
	 * @param targetYearMonth
	 * @param status
	 * @param model
	 * @return
	 */
	@PostMapping(path = "/regist", params = "apprication")
	public String approve(@RequestParam(required = false) Integer year,@RequestParam(required = false) Integer month, HttpSession session, Model model,
			RedirectAttributes redirectAttribute) {

		LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		Integer userId = loginUser.getUserId();
		// ステータス（承認状況）の表示
		String statusMessage = attendanceService.checkStatus(userId, year, month);
		model.addAttribute("statusMessage", statusMessage);

		String message = attendanceService.getMonthlyAttendance(userId, year, month);
		redirectAttribute.addFlashAttribute("message", message);

		model.addAttribute(message);

		return "redirect:/attendance/regist";
	}
	

}

  // 備考欄の入力チェック
//if (attendanceForm.getRemarks() != null && attendanceForm.getRemarks().matches("^[\\uFF21-\\uFF3A\\uFF41-\\uFF5A]+$")) {
//	model.addAttribute("errorMessage", "※全角で記入してください。");
//	
//} else if (attendanceForm.getRemarks() != null && 20 < attendanceForm.getRemarks().length()) {
//	model.addAttribute("errorMessage", "※20字以内で入力してください。");
//}