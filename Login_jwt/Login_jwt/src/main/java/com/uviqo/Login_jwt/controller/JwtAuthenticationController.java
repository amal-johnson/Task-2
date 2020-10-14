package com.uviqo.Login_jwt.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.uviqo.Login_jwt.config.JwtTokenUtil;
import com.uviqo.Login_jwt.model.JwtRequest;
import com.uviqo.Login_jwt.model.UserDTO;
import com.uviqo.Login_jwt.repository.UserDao;
import com.uviqo.Login_jwt.service.JwtUserDetailsService;

//This class is for Authentication and registration
@Controller
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private UserDao repo;
	
	@Autowired
	private PasswordEncoder bCryptEncoder;
	
	@Autowired
	private RestTemplate restTemplate;
	
	//Display Registration page
	@RequestMapping("/register")
	public String register()
	{
		return "register";
	}
		
	
	//Gets the form data to register new user, goes to registration microservice
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public String RegistrationMicroservice(UserDTO user,Model model) 
	{
		String result="null";
		String pass="null";
		
		//if entered passwords are same, proceed
		if(user.getPassword().equals(user.getPasswordc())){
			String uname=user.getUsername();
				
			//encrypt password
			String pw=bCryptEncoder.encode(user.getPassword());
				
			//encode encrypted password into URL safe string
			try 
			{
				pass=URLEncoder.encode(pw,StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException ex) {
	            throw new RuntimeException(ex.getCause());
	        }
					
			try 
			{
				//check for existing username
				if(repo.findByUsername(user.getUsername())==null)
				{	
					//goes to registration microservice for registration
					result=restTemplate.getForObject("http://localhost:8081/registerportal/"+uname+"/"+pass,String.class);
						
					//checks what is returned from the registration microservice
					if(result.equals("succes"))
						model.addAttribute("match", "Registration succesfull");
					else 
						model.addAttribute("match", "Registration failed,Try again");
						return "register";
					}
				//if userName already exists
				else
				{
					model.addAttribute("match", "Username already exists");
					return "register";
				}
				//if error occurred on Rest call
				}catch(Exception e) {
					model.addAttribute("match", "Something went wrong");
					return "register";
				}	
		}
		else 
		{
			model.addAttribute("match","Passwords don't match");
			return "register";
		}	
	}
	
	
	//check if user is already logged in(Existing cookie)
	@SuppressWarnings("unused")
	@RequestMapping(value={"/authenticate","/"})
	public String check_if_logged_in(Model model,HttpServletRequest req, HttpServletResponse res) 
	{	
		Cookie cookie = null;
		String uname=null;
		
		//read all cookies in request to an array
		Cookie[] cookies = req.getCookies();
		try 
		{	
		for (int i = 0; i < cookies.length; i++)
		{
			cookie=cookies[i];
			//get cookie with Authorization(token)
		    if ("Authorization".equals(cookie.getName()))
		    {
		    	//gets the token of existing user
		    	String existing_token=cookie.getValue();
		    	
		    	//reads the userName from token
		    	uname=jwtTokenUtil.getUserNameFromToken(existing_token.substring(7));
		    	
		    	//add existing users details to homePage
				model.addAttribute("UserName",uname);
				model.addAttribute("token",existing_token);
				return "homepage";
		    }
		    else
		    {
		    	//if user isn't logged in(cookie not present) return login page
		    	model.addAttribute("error","Please login to continue");
		    	return "login";
		    }
		  }
		
		}catch(Exception e) {
			System.out.println("Something went Wrong");		
		}
		model.addAttribute("error", "Please login again");
		return "login";
	}
	
	
	//Authentication, generation of token and cookie
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public String createAuthenticationToken(JwtRequest request, Model model,HttpServletRequest req, HttpServletResponse res) throws Exception 
	{		
		try 
		{
			//Authenticate the user with springs built in security method
			authenticate(request.getUsername(), request.getPassword());
		}catch (Exception e) {
			//if authentication failed
			model.addAttribute("error", "Invalid credentials");
			return "login";
		}
		
		//UserDetails provides current user information and security information
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		//use final to define an variable that can only be assigned once
		final String tok = jwtTokenUtil.generateToken(userDetails);
		//Add the bearer_ string in front of token
		final String token="Bearer_"+tok;
		
		//Add the token in an http cookie, it'll be stored in user's browser 
		int COOKIE_MAX_AGE=5*60*1000;
		//create cookie token_cookie with name as Authorization, value token
		Cookie token_cookie = new Cookie("Authorization", token);
        token_cookie.setMaxAge(COOKIE_MAX_AGE);
        token_cookie.setSecure(false);
        //to protects cookie from client side scripts
        token_cookie.setHttpOnly(true);
        //cookie added to the response to client, this cookie will be stored at client side
        res.addCookie(token_cookie);
        
        //Add username and the generated token to home page
		model.addAttribute("UserName",request.getUsername());
		model.addAttribute("token",token);
		return "homepage";
	}
	
	//executes when user tries to login
	private void authenticate(String username, String password) throws Exception {
		try {
			//Springs AuthenticationManager.authenticate method, pass uname and password to authenticate user
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);

		}
	}

	

}
