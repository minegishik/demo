package com.example.demo.entity;

import java.sql.Time;
import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class AttendanceUser {
	
	//ユーザーID
	private Integer userId;
	//勤務状況
	private Short status;
	//日付
	private Date date;
	//出勤時間
	private Time startTime;
	//退勤時間
	private Time endTime;
	//備考
	private String remarks;
	//勤怠ID
	private Integer attendanceId;
		

}
