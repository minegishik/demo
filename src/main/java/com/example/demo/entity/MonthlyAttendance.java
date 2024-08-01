package com.example.demo.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class MonthlyAttendance {
	// 申請ID
	private Integer id;
	// 申請者ID
	private Integer userId;
	// 対象年月
	@DateTimeFormat(pattern = "yyyy-MM")
	private Date targetYearMonth;
	// 申請日
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	// 承認状況
	private Short status;

}
