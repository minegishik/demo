package com.example.demo.entity;

import java.sql.Time;
import java.util.Date;

import lombok.Data;

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

}
