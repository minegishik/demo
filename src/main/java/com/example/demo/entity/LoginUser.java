package com.example.demo.entity;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class LoginUser {
	//ユーザーID
	private Integer userId;
	//パスワード
	private String password;
	//ユーザー名
	private String name;
	//権限
	private String role;
	//作成日
	private Date startDate;
	//部署ID
	private Integer departmentId;
}
