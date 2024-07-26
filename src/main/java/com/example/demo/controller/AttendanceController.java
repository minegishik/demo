package com.example.demo.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.AttendanceUser;
import com.example.demo.entity.LoginUser;
import com.example.demo.form.AttendanceForm;
import com.example.demo.form.AttendanceFormList;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.LoginService;
import com.example.demo.util.TimeUtils;

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

		if (year == null || month == null) {
			model.addAttribute("errorMessage", "※年月を指定してください");
			return "attendance/regist"; // 同じ画面に戻る
		}

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

			// 時間の分割処理
			Map<String, String> startTimeParts = TimeUtils.splitTime(day2.getStartTime());
			attendanceForm.setStartHour(startTimeParts.get("hour")); // 時
			attendanceForm.setStartMinute(startTimeParts.get("minute")); // 分

			Map<String, String> endTimeParts = TimeUtils.splitTime(day2.getEndTime());
			attendanceForm.setEndHour(endTimeParts.get("hour")); // 時
			attendanceForm.setEndMinute(endTimeParts.get("minute")); // 分

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
	@PostMapping(path = "/regist", params = "regist")
	public String punchIn(@ModelAttribute AttendanceForm attendanceForm, Model model) {
		
		return "attendance/regist";
	}

}

  // 備考欄の入力チェック
//if (attendanceForm.getRemarks() != null && attendanceForm.getRemarks().matches("^[\\uFF21-\\uFF3A\\uFF41-\\uFF5A]+$")) {
//	model.addAttribute("errorMessage", "※全角で記入してください。");
//	
//} else if (attendanceForm.getRemarks() != null && 20 < attendanceForm.getRemarks().length()) {
//	model.addAttribute("errorMessage", "※20字以内で入力してください。");
//}