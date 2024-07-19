package com.example.demo.dto;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class AttendanceDto {
	
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
	
	// getDate() が返す java.sql.Date を LocalDate に変換するメソッド
    public LocalDate getLocalDate() {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
