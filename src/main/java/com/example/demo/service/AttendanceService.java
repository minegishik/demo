package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AttendanceDto;
import com.example.demo.mapper.AttendanceMapper;

@Service
public class AttendanceService {
	
	@Autowired
	AttendanceDto attendanceDto;
	@Autowired
	AttendanceMapper attendanceMapper;
	
	
	// 時間をHH:mm形式にフォーマットするメソッド
    public String formatTimeToHHMM(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
	/**
	 * 勤怠一覧情報取得
	 * 
	 * @param userId
	 * @return 勤怠登録画面用DTOリスト
	 * 
	 */
	public List<AttendanceDto> getAttendanceInfo(Integer userId){
		
		List<AttendanceDto> attendanceDtoList = attendanceMapper.getAttendanceInfo(userId);
		
		return attendanceDtoList;
	}
	
	/**
	 * 勤怠情報年月の取得
	 * 
	 * @param userId
	 * @param year
	 * @param month
	 * @return
	 */
	public List<AttendanceDto> findByAttendanceYearMonth(Integer userId, int year, int month) {
         
        return attendanceMapper.findByAttendanceYearMonth(userId, year, month);
    }
	/**
     * 指定されたユーザーID、年、月に対応する日付のリストを取得する
     * 
     * @param userId ユーザーID
     * @param year 年
     * @param month 月
     * @return 日付のリスト
     */
    public List<LocalDate> getDatesByYearMonth(Integer userId, int year, int month) {
        List<LocalDate> dates = new ArrayList<>();
        
        // 指定された年月に対応する勤怠情報を取得
        List<AttendanceDto> attendanceList = findByAttendanceYearMonth(userId, year, month);
        
        // 勤怠情報から日付のリストを作成
        for (AttendanceDto attendance : attendanceList) {
            LocalDate date = attendance.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dates.add(date);
        }
        
        return dates;
    }
	
	
	/**
     * 指定された年のリストを取得する
     * 
     * @return 年のリスト
     */
    public List<Integer> getAllYears() {
        // 例として2022年から2025年までの年をリストとして返す
        return Arrays.asList(2022, 2023, 2024, 2025);
    }

    /**
     * すべての月のリストを取得する
     * 
     * @return 月のリスト
     */
    public List<Map<String, String>> getAllMonths() {
        List<Map<String, String>> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, String> monthMap = new HashMap<>();
            monthMap.put("key", String.valueOf(i));
            monthMap.put("value", i + "月");
            months.add(monthMap);
        }
        return months;
    }

	
	
}
