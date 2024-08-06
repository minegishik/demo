package com.example.demo.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class MonthlyAttendanceDto {
	// 申請ID
		private Integer id;
		// 申請者ID
		private Integer userId;
		// 申請対象年月
		@DateTimeFormat(pattern = "yyyy-MM")
		private Date targetYearMonth;
		// 申請日
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date date;
		// 承認状況
		private Short status;
		//ユーザー名
		private String name;
		//年
		private Integer year;
		//月
		private Integer month;

}
