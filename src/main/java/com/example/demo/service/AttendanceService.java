package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AttendanceUser;
import com.example.demo.mapper.AttendanceMapper;

@Service
public class AttendanceService {
	

	@Autowired
	AttendanceMapper attendanceMapper;
	
	
	// 時間をHH:mm形式にフォーマットするメソッド
    public String formatTimeToHHMM(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
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
	        attendanceUser.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	        calendar.add(attendanceUser);
	    }

	    return calendar;
	}
	
}
