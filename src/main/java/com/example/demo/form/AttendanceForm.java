package com.example.demo.form;

import java.sql.Time;
import java.util.Date;

import lombok.Data;

@Data
public class AttendanceForm {
	
	private Integer attendanceId;
	private Integer userId;
	private Time startTime;
	private Time endTime;	
	private Date date;
	private String remarks;
	private Short status;
	
	private String formattedDate; // フォーマットされた日付を保持する文字列
	private String formattedWeek; // フォーマットされた曜日を保持する文字列

}
