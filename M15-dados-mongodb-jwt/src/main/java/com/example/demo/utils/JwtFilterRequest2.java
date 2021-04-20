package com.example.demo.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.UserService;


@Component
public class JwtFilterRequest2 extends OncePerRequestFilter{
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserService userService;
	@Autowired
	private PlayerRepository playerDao;
	


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			String authorizationHeader = request.getHeader("Authorization");
			String path = request.getRequestURI();
			if (! path.matches("/auth")) {
				String playerName = null;
				if(! String.valueOf(path.subSequence(path.lastIndexOf("home/players/")+13, path.indexOf("/games"))).isEmpty()) {

					String subs = String.valueOf(path.subSequence(path.lastIndexOf("home/players/")+13, path.indexOf("/games")));
					long playerId = Long.parseLong(subs);
					playerName = playerDao.findById(playerId).get().getName();
				}
				String username = null;
				String jwtToken = null;
				if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
					jwtToken = authorizationHeader.substring(7);
					username = jwtUtils.extractUsername(jwtToken);
				}
				if (! username.equalsIgnoreCase(playerName)) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "No se ha autentificado para este usuario");
				}/*
				if(username != null  && username.equalsIgnoreCase(playerName)) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "No se ha autentificado para este usuario");
					UserDetails currentUserDetails = userService.loadUserByUsername(username);
					Boolean tokenValidated = jwtUtils.validateToken(jwtToken, currentUserDetails);
					if (tokenValidated) {
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
								new UsernamePasswordAuthenticationToken(currentUserDetails,null,currentUserDetails.getAuthorities()) ;
						usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				}*/
				
			}

			filterChain.doFilter(request, response);
		
	}
}