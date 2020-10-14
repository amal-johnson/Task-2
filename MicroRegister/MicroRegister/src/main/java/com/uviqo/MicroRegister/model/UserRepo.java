package com.uviqo.MicroRegister.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User1,Integer> {
	
	User1 findByUsername(String userName);

}
