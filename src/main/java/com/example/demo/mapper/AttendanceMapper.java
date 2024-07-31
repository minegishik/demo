package com.example.demo.mapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	 * @return 勤怠情報エンティティ
	 */
	List<AttendanceUser>findByAttendanceYearMonth(@Param("userId") int userId, @Param("year") int year, @Param("month") int month);
	
	
	/**
	 * 勤怠情報登録
	 * 
	 * @param attendanceUser
	 * @return 登録結果
	 */
	int insertAttendance (AttendanceUser attendanceUser);
	
	/**
	 * 勤怠情報更新
	 * 
	 * @param attendanceUser
	 * @return 反映結果
	 */
	Boolean updateAttendance (AttendanceUser attendanceUser);
	
	/**
	 * 勤怠情報削除
	 * 
	 * @param attendanceUser
	 * @return 反映結果
	 */
	void deleteAttendance (@Param("userId") Integer userId, @Param("date") LocalDate date);
}
