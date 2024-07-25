package com.example.demo.form;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserForm {

	//ユーザーID
	private Integer userId;
	//パスワード
	private String password;
	//ユーザー名
	private String name;
	//権限
	private String role;
	//作成日
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String startDate;
	//部署ID
	private Integer departmentId;
	
}
	


