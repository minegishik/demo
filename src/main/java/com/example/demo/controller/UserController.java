package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
	 @RequestMapping("user/management")
	 public String user() {
		 return "user/management";
	 }
	 
	 
	

}
