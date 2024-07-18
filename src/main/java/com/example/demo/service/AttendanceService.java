package com.example.demo.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
	
	
}
