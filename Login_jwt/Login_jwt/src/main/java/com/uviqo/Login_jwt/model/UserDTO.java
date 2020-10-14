package com.uviqo.Login_jwt.model;
//This class is used for Reading data from Register page, DTO is DataTransferObject. 
public class UserDTO {
	private String Username;
	private String Password;
	private String Passwordc;
	
	public UserDTO() {
	}
	
	public UserDTO(String username, String password, String passwordc) {
		super();
		Username = username;
		Password = password;
		Passwordc = passwordc;
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
	public String getPasswordc() {
		return Passwordc;
	}
	public void setPasswordc(String passwordc) {
		Passwordc = passwordc;
	}
	
	
	
	
	
	
}
	