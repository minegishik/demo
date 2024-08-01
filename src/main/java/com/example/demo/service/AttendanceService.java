package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MonthlyAttendanceDto;
import com.example.demo.entity.AttendanceUser;
import com.example.demo.entity.MonthlyAttendance;
import com.example.demo.mapper.AttendanceMapper;

@Service
public class AttendanceService {
	

	@Autowired
	AttendanceMapper attendanceMapper;
	
	
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
	 * 申請該当月の月次勤怠情報取得・申請処理
	 * 
	 * @param userId
	 * @param targetYearMonth
	 * @param date
	 * @return
	 */
	public String getMonthlyAttendance(Integer userId, Integer year, Integer month) {

		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate targetYearMonth = yearMonth.atDay(1);
		
		System.out.println(year);
		System.out.println(month);

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
			attendanceMapper.update(request);
			

			System.out.println(searchResult);
			return "承認申請を更新しました。";
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
		System.out.println(dto.getStatus());
		
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
	
}
