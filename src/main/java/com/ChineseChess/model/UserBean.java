package com.ChineseChess.model;

import java.io.Serializable;

public class UserBean implements Serializable{

	/**
	 * 用户名
	 */
	private String userName ;
	/**
	 * 密码
	 */
	private String userPwd ;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
}
