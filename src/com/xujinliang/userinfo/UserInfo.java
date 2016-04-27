package com.xujinliang.userinfo;

import android.app.Application;

public  class UserInfo extends Application{
	private String name;
	private String code;
	private String nickname;
	private String email;
	@Override
	public void onCreate()
	{
		super.onCreate();
	    insideSetValue(); 
	}
	public void insideSetValue() {
		name = "";
		code = "";
		nickname = "";
		email = "";
	}
	public String getName() {
		return this.name;
		
	}
	public String getCode() {
		return this.code;
	}
	public String getNickname() {
		return this.nickname;
	}
	public String getEmail() {
		return this.email;
	}
	
	public void setValue (String name,String code,String nickname,String email) {
		this.name = name;
		this.code = code;
		this.nickname = nickname;
		this.email = email;
	}
}
