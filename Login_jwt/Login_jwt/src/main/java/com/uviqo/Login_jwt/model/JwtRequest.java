package com.uviqo.Login_jwt.model;

import java.io.Serializable;
//Used To read data from Login page
public class JwtRequest implements Serializable{
	
	private static final long serialVersionUID = 5926468583005150707L;
	private String Username;
	private String Password;
	
	public JwtRequest() {
	}
	
	public JwtRequest(String username, String password) {
		super();
		Username = username;
		Password = password;
	}

	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
