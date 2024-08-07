package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.MonthlyAttendanceDto;
import com.example.demo.entity.AttendanceUser;
import com.example.demo.entity.MonthlyAttendance;
import com.example.demo.form.AttendanceForm;
import com.example.demo.form.AttendanceFormList;
import com.example.demo.mapper.AttendanceMapper;
import com.example.demo.util.LoginUserUtil;

@Service
public class AttendanceService {
	

	@Autowired
	private AttendanceMapper attendanceMapper;
	@Autowired
	private LoginUserUtil loginUserUtil;
	
	
	// 時間をHH:mm形式にフォーマットするメソッド
    public String formatTimeToHHMM(LocalTime time) {
    	if (time != null) {
            return time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            return ""; // もしくは他の適切な処理を追加する（例：デフォルト値の設定、エラーメッセージの返却など）
        }
    }
	
	
	/**
	 * 年月から勤怠情報の取得
	 * 
	 * @param userId
	 * @param year
	 * @param month
	 * @return
	 */
	public List<AttendanceUser> getAttendanceYearMonth(int userId,int year, int month) {
         
		List<AttendanceUser> attendanceList = attendanceMapper.findByAttendanceYearMonth(userId,year,month);
		
        return attendanceList;
    }
	
	
	/**
	 * カレンダーの日付を取得
	 * 
	 * 
	 */
	public List<AttendanceUser> getCalendar(int year, int month) {
	    YearMonth yearMonth = YearMonth.of(year, month);
	    LocalDate firstDayOfMonth = yearMonth.atDay(1);
	    LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
	    List<AttendanceUser> calendar = new ArrayList<>();

	    for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
	        // AttendanceUserのインスタンスを生成し、日付を設定する
	        AttendanceUser attendanceUser = new AttendanceUser();
	        attendanceUser.setDate(date);
	        calendar.add(attendanceUser);
	    }

	    return calendar;
	}
	
	/**
	 * 勤怠登録処理
	 * 
	 * @param attendanceForm
	 * @param year
	 * @param month
	 * @param userId
	 * @return
	 */
    public boolean insertAttendance(AttendanceUser attendanceUser) {
    	int attendance = attendanceMapper.insertAttendance(attendanceUser);
        
    	return attendance > 0;
    }
    
    /**
     * 勤怠情報削除
     * 
     * @param userId
     * @param date
     */
    public void deleteAttendance(Integer userId,LocalDate date) {
    	attendanceMapper.deleteAttendance(userId, date);
    }
    
    
    /**
     * 勤務状況と出退勤時間のエラーチェック
     * 
     * @param attendanceForm
     * @param userId
     * @param date1
     * @param result
     * @return
     */
	public Boolean errorCheck(AttendanceFormList attendanceFormList, BindingResult result, Model model) {
//		AttendanceForm attendanceForm = new AttendanceForm();
		boolean errorFlg = false;
		for (int i = 0; i < attendanceFormList.getAttendanceFormList().size() ; i++) {
		
			AttendanceForm attendanceForm = attendanceFormList.getAttendanceFormList().get(i);

		if (attendanceForm.getStatus() != null) {
			if (attendanceForm.getStatus() == 1 || attendanceForm.getStatus() == 2 || attendanceForm.getStatus() == 4
					|| attendanceForm.getStatus() == 5 || attendanceForm.getStatus() == 9
					|| attendanceForm.getStatus() == 11) {
				if (attendanceForm.getStartHour() != null || attendanceForm.getStartMinute() != null
						|| attendanceForm.getEndHour() != null || attendanceForm.getEndMinute() != null) {
					errorFlg = true;
				}
			} else {
				if (attendanceForm.getStartHour() == null || attendanceForm.getStartMinute() == null
						|| attendanceForm.getEndHour() == null || attendanceForm.getEndMinute() == null) {
					errorFlg = true;
				}

			}
//			model.addAttribute("errorCheck", "エラー");
		}
		
		}
		return errorFlg;

	}
    	
//    	if (searchResult.getStatus() == 1 || searchResult.getStatus() == 2 || searchResult.getStatus() == 4 || searchResult.getStatus() == 5 || searchResult.getStatus() == 9 || searchResult.getStatus() == 11) {
//    		if (searchResult.getStartTime() != null || searchResult.getEndTime() != null) {
//    			String errorCheckMessage = "指定された状態では、時間を入力することはできません。";
//    			return errorCheckMessage;
//    		} else {
//    			return "";
//    		}
//    	} else {
//    		if (searchResult.getStartTime() == null || searchResult.getEndTime() == null) {
//    			String errorCheckMessage = "指定された状態では、時間が必須です。";
//    			return errorCheckMessage;
//    		} else {
//    			return "";
//    		}
//    	}
	
    
    /**
	 * 申請該当月の月次勤怠情報取得・申請処理
	 * 
	 * @param userId
	 * @param targetYearMonth
	 * @param date
	 * @return
	 */
	public String getMonthlyAttendance(Integer userId, Integer year, Integer month, LocalDate date) {

		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate targetYearMonth = yearMonth.atDay(1);
		

		MonthlyAttendance searchResult = attendanceMapper.findMonthlyAttendanceByUserAndMonth(userId,
				java.sql.Date.valueOf(targetYearMonth));

		MonthlyAttendance request = new MonthlyAttendance();
		request.setUserId(userId);
		request.setTargetYearMonth(java.sql.Date.valueOf(targetYearMonth));
		request.setDate(java.sql.Date.valueOf(LocalDate.now()));
		request.setStatus((short) 1);
		

		if (searchResult == null) {
			attendanceMapper.appricationMonthlyAttendance(request);
			return "承認申請が完了しました。";

		} else {
			
			request.setId(searchResult.getId());
			request.setStatus((short) 1);
			attendanceMapper.test(request);

			return "再申請が完了しました。";
		}

	}

	/**
	 * ステータス表示処理
	 * 
	 * @param userId
	 * @param year
	 * @param month
	 * @return
	 */
	public String checkStatus(Integer userId, Integer year, Integer month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate targetYearMonth = yearMonth.atDay(1);

		List<MonthlyAttendanceDto> searchResult = attendanceMapper.findMonthlyAttendanceByUserAndMonth2(userId,
				java.sql.Date.valueOf(targetYearMonth));

		if (searchResult == null || searchResult.isEmpty()) {
			return "未申請";
		} 
		MonthlyAttendanceDto dto = searchResult.get(0);
		Short status = dto.getStatus();
		
		
		// ステータスに応じて結果を返す
		switch (status) {
		case 1:
			return "申請中";
		case 2:
			return "承認済";
		case 3:
			return "却下";
		default:
			return "未申請";
		}
		

	}
	
	
	/**
	 * 月次勤怠情報取得
	 * 
	 * @param userId
	 * @param targetYearMonth
	 * @return
	 */
	public List<MonthlyAttendanceDto> getMonthlyAttendanceReq() {
		
		
		List<MonthlyAttendanceDto> monthlyAttendanceDtoList = new ArrayList<>();
		
		if(loginUserUtil.isManager()) {
			
			monthlyAttendanceDtoList = attendanceMapper.monthlyAttendanceReqList();
			
		} else {
			throw new SecurityException("権限がありません。");
		}
		
		 
		 
		return monthlyAttendanceDtoList;
	}
	
	/**
	 * 承認申請 却下処理
	 * 
	 * @param userId
	 * @param targetYearMonth
	 * @param monthlyAttendance
	 */
	public void rejected(AttendanceFormList formList, Integer userId, Integer year, Integer month) {
			
		attendanceMapper.rejectApprovalStatus(formList.getAttendanceFormList().get(0).getUserId(), formList.getAttendanceFormList().get(0).getDate());
		
	}
	
	/**
	 * 承認申請 承認処理
	 * 
	 * @param userId
	 * @param targetYearMonth
	 * @param monthlyAttendance
	 */
	public void approve(AttendanceFormList formList) {
		attendanceMapper.approvalStatus(formList.getAttendanceFormList().get(0).getUserId(), formList.getAttendanceFormList().get(0).getDate());
	}
	
}
