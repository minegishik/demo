package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.MonthlyAttendanceDto;
import com.example.demo.entity.AttendanceUser;
import com.example.demo.entity.LoginUser;
import com.example.demo.form.AttendanceForm;
import com.example.demo.form.AttendanceFormList;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.LoginService;
import com.example.demo.util.LoginUserUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {
	@Autowired
	LoginService loginService;
	@Autowired
	AttendanceService attendanceService;
	@Autowired
	LoginUserUtil loginUserUtil;

	/**
	 * 勤怠登録画面 初期表示
	 * 
	 * 
	 * @return　勤怠登録画面
	 */
	@RequestMapping("/regist")
	public String regist(HttpSession session, Model model,Integer userId) {
		

		LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		if(loginUserUtil.isManager()) {
		List<MonthlyAttendanceDto> monthlyAttendanceDtoList = attendanceService.getMonthlyAttendanceReq(userId);
		model.addAttribute("monthlyAttendanceDtoList", monthlyAttendanceDtoList);
		System.out.println(monthlyAttendanceDtoList);
		}
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
		
		// 時間と分のリストを生成
	    List<String> hours = IntStream.range(0, 24)
	            .mapToObj(i -> String.format("%02d", i))
	            .collect(Collectors.toList());
	    List<String> minutes = IntStream.range(0, 60)
	            .mapToObj(i -> String.format("%02d", i))
	            .collect(Collectors.toList());

	    model.addAttribute("hours", hours);
	    model.addAttribute("minutes", minutes);
	    
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
			
			
			// 時間の分割処理
	        if (day2.getStartTime() != null) {
	            attendanceForm.setStartHour(String.format("%02d", day2.getStartTime().getHour())); // 時
	            attendanceForm.setStartMinute(String.format("%02d", day2.getStartTime().getMinute())); // 分
	        }
	        if (day2.getEndTime() != null) {
	            attendanceForm.setEndHour(String.format("%02d", day2.getEndTime().getHour())); // 時
	            attendanceForm.setEndMinute(String.format("%02d", day2.getEndTime().getMinute())); // 分
	        }


			form.add(attendanceForm); // フォームリストに追加する

		}

		formList.setAttendanceFormList(form);
		model.addAttribute("formList", formList);
		
		// 時間の選択肢の準備
	    model.addAttribute("hours", hours);
	    model.addAttribute("minutes", minutes);

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

			 // 出勤時間と退勤時間の処理
	        LocalTime startTime = parseTime(attendanceForm.getStartHour(), attendanceForm.getStartMinute());
	        LocalTime endTime = parseTime(attendanceForm.getEndHour(), attendanceForm.getEndMinute());
			
			LocalDate date = attendanceForm.getDate();
			
			if(attendanceForm.getStatus() != null && attendanceForm.getDate() != null) {
				
				
				
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
		    newAttendance.setStartTime(startTime);
            newAttendance.setEndTime(endTime);
			newAttendance.setRemarks(attendanceForm.getRemarks());
			
			
			
			attendanceService.insertAttendance(newAttendance);
			}
		}
		
		
		

		// 成功メッセージの設定
	    model.addAttribute("successMessage", "勤怠登録が完了しました。");

	 
        return displayIn(selectedYear, selectedMonth, formList, session, model);
	}
	
	// 時間を LocalTime に変換するヘルパー関数
	private LocalTime parseTime(String hourStr, String minuteStr) {
	    if (hourStr == null || hourStr.isEmpty() || minuteStr == null || minuteStr.isEmpty()) {
	        return null; // 無効な場合は null
	    }

	    try {
	        int hour = Integer.parseInt(hourStr);
	        int minute = Integer.parseInt(minuteStr);

	        // 時間が 00 で分も 00 の場合は null を返す
	        if (hour == 0 && minute == 0) {
	            return null;
	        }

	        // 分が 00 の場合も、時間が 00 でなければ保存する
	        if (minute == 0 && hour != 0) {
	            return LocalTime.of(hour, minute);
	        }

	        return LocalTime.of(hour, minute);
	    } catch (NumberFormatException e) {
	        return null; // 無効な数値の場合も null
	    }
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
	
	/**
	 * 勤怠登録画面　『却下』ボタン押下
	 * 
	 * @return マネージャー用勤怠管理画面
	 */
	public String reject() {
		return "attendance/regist";
	}
	
	
	/**
	 * 勤怠登録画面 『承認』ボタン押下
	 * 
	 * @return マネージャー用勤怠管理画面
	 */
	public String permit() {
		return "attendance/regist";
	}
	

}
