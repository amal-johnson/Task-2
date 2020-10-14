package com.uviqo.Login_jwt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user1")
public class DAOUser {
	@Id
	private long id;
	@Column
	private String username;
	@Column
	private String passw;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return passw;
	}
	public void setPassword(String password) {
		this.passw = password;
	}
	
}
