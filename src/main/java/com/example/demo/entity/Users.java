package com.example.demo.entity;

import lombok.Data;

@Data
public class Users {
	//ユーザーID
	private Integer userId;
	//パスワード
	private String password;
	//ユーザー名
	private String name;
	//権限
	private String role;
	//作成日
	private String startDate;
	//部署ID
	private Integer departmentId;

}


// StartDate … String型