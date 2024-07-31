package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class AttendanceUser {

	//勤怠ID
	private Integer attendanceId;
	//ユーザーID
	private Integer userId;
	//勤務状況
	private Integer status;
	//日付
	private LocalDate date;
	//出勤時間
	private LocalTime startTime;
	//退勤時間
	private LocalTime endTime;
	//備考
	private String remarks;
	//フォーマット変更した月日
	private String formattedDate;
	//フォーマット変更した曜日
	private String formattedWeek;
	
	// コンストラクタ
    public AttendanceUser() {
        // デフォルトコンストラクタ
    }
  


}
