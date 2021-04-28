package com.example.demo.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JwtFilterRequest extends OncePerRequestFilter{
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserService userService;
	


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			String authorizationHeader = request.getHeader("Authorization");
			String username = null;
			String jwtToken = null;
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwtToken = authorizationHeader.substring(7);
				try {
					username = jwtUtils.extractUsername(jwtToken);
				}catch(ExpiredJwtException e) {
					
				}
			}
			
			if(username != null && SecurityContextHolder.getContext().getAuthentication() ==null ) {
				UserDetails currentUserDetails = userService.loadUserByUsername(username);
				Boolean tokenValidated = jwtUtils.validateToken(jwtToken, currentUserDetails);
				if (tokenValidated) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
							new UsernamePasswordAuthenticationToken(currentUserDetails,null,currentUserDetails.getAuthorities()) ;
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			
			filterChain.doFilter(request, response);
		
	}
}