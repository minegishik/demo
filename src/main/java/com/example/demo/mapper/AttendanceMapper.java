package com.example.demo.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.AttendanceDto;
import com.example.demo.entity.AttendanceUser;

@Mapper
public interface AttendanceMapper {
	
	/**
	 * 勤怠情報取得
	 * 
	 * @param userId
	 * 
	 * @return 勤怠情報エンティティ
	 */
	List<AttendanceUser> findByAttendanceUserId(@Param("userId") Integer userId);
	
	/**
	 * 勤怠情報取得（ユーザーID＆日付）
	 * 
	 * @param userId
	 * @param date
	 * 
	 * @return 勤怠情報エンティティ
	 */
	
	AttendanceUser findByAttendanceDate(
			@Param("userId") Integer userId,@Param("date") Date date);
	
	
	
	/**
	 * 勤怠情報年月の取得
	 * 
	 * @param userId
	 * @param year
	 * @param month
	 * 
	 * @return 
	 */
	List<AttendanceDto>findByAttendanceYearMonth(@Param("userId") int userId, @Param("year") int year, @Param("month") int month);

}
