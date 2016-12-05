package com.pagp.medicalweb.web.core;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import com.pagp.medicalweb.web.core.dto.JwtAuthenticationToken;
import com.pagp.medicalweb.web.core.exceptions.JwtTokenMissingException;


@Component("jwtAuthenticationFilter")
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	
	public JwtAuthenticationFilter() {
		super("/**");
	}

	
	@Autowired
	@Qualifier("authenticationManager")
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}
	
	@Autowired
	@Qualifier("jwtAuthenticationFailureHandler")
	public void setAuthenticationFailureHandler(JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler) {
		super.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);
	};
	
	@Autowired
	@Qualifier("jwtAuthenticationSuccessHandler")
	public void setAuthenticationSuccessHandler(JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler) {
		super.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);
	};


	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			throw new JwtTokenMissingException("No JWT token found in request headers");
		}

		String authToken = header.substring(7);

		JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);

		return getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);

		// As this authentication is in HTTP header, after success we need to
		// continue the request normally
		// and return the response as if the resource was not secured at all
		chain.doFilter(request, response);
	}
}