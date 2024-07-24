package com.example.demo.dto;

import java.sql.Time;
import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class AttendanceDto {
	
	//勤怠ID
	private Integer attendanceId;
	//ユーザーID
	private int userId;
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

}
