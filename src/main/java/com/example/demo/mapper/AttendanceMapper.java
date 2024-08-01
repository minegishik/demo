package com.example.demo.mapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.MonthlyAttendanceDto;
import com.example.demo.entity.AttendanceUser;
import com.example.demo.entity.MonthlyAttendance;
import com.example.demo.form.AttendanceForm;

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
	
	
	/**
	 * 月次勤怠の承認申請
	 * 
	 * @param userId
	 * @param targetYearMonth
	 * @param status
	 * @return
	 */
	// 申請ID、申請者ID、申請対象年月、申請日、ステータス(承認待ち・承認済み・却下)
	
	Boolean appricationMonthlyAttendance(AttendanceForm attendanceForm);
	
	void appricationMonthlyAttendance(MonthlyAttendance monthlyAttendance);
	
	MonthlyAttendance findMonthlyAttendanceByUserAndMonth(@Param("userId")Integer uerId, @Param("targetYearMonth") Date targetYearMonth);
		
//	MonthlyAttendance findMonthlyAttendanceByUserAndMonth(@Param("userId")Integer uerId, @Param("targetYearMonth") Date targetYearMonth);
	
	List<MonthlyAttendanceDto> findMonthlyAttendanceByUserAndMonth2(@Param("userId")Integer uerId, @Param("targetYearMonth") Date targetYearMonth);
	
	void update(MonthlyAttendance monthlyAttendance);

	void update(MonthlyAttendanceDto searchResult);
}
