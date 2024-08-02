package com.example.demo.form;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AttendanceForm {
	
	private Integer attendanceId;
	private Integer userId;
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalDate date;
	private String remarks;
	private Integer status;

	private String formattedDate; // フォーマットされた日付を保持する文字列
	private String formattedWeek; // フォーマットされた曜日を保持する文字列
	private String startHour;
	private String startMinute;
	private String endHour;
	private String endMinute;

}
