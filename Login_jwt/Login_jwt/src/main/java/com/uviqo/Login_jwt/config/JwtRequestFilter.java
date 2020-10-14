package com.uviqo.Login_jwt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uviqo.Login_jwt.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
			,FilterChain chain)throws ServletException, IOException {
		
		//Token is stored in HTTPcookie, need to read it from HttpRequest
		String cookieTokenHeader=null;
		
		//Get all cookies to cookie array
		Cookie[] cookies = request.getCookies();
		try {
		for (int i = 0; i < cookies.length; i++)
		{
			//Find the cookie value where cookie name=Authorization
		    if ("Authorization".equals(cookies[i].getName()))
		    {
		    	//reads the Authorization cookie to cookieTokenHeader
		    	 cookieTokenHeader = cookies[i].getValue();
		    }
		  }
		}catch(Exception e) {
			System.out.println("Could not process cookie");
		}
		 
		String username = null;
		String jwtToken = null;
		
		if (cookieTokenHeader != null && cookieTokenHeader.startsWith("Bearer_")) {
			// JWT Token is in the form "Bearer_ token", Remove Bearer_ to obtain token 
			jwtToken = cookieTokenHeader.substring(7);
			
			try {
				username = jwtTokenUtil.getUserNameFromToken(jwtToken);
				} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
			
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		
		
		// Once we get the token validate it.
		//SecuritycontextHolder-Associates a given SecurityContext with the current execution thread.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//load the user from the database
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
				= new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So its passed to the
				// Spring Security Configurations 
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}
	
	

}