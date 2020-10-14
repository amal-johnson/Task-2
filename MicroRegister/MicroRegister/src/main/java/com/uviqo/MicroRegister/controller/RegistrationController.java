package com.uviqo.MicroRegister.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.uviqo.MicroRegister.model.User1;
import com.uviqo.MicroRegister.model.UserRepo;

@RestController
public class RegistrationController {
	
	RestTemplate restTemplate= new RestTemplate();
	
	@Autowired
	private UserRepo userRepo;
	
	@RequestMapping("/registerportal/{userName}/{Password}")
	public String registerUser(@PathVariable("userName") String username,
			@PathVariable("Password") String password) {
		String pword=null;
		try {
			//decode the encoded Password(URL encoded)
            pword=URLDecoder.decode(password, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
		
		User1 u=new User1();
		u.setUsername(username);
		u.setPassword(pword);
		try {
		userRepo.save(u);
		}catch (Exception e) { 
			System.out.println("Something went wrong in registration");
			return "failed";
		}
		return "succes";
	}
}
