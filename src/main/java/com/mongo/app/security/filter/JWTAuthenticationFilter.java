package com.mongo.app.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongo.app.models.Usuario;
import com.mongo.app.security.IJWTService;
import com.mongo.app.security.JWTServiceImpl;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private Logger logger= LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	private AuthenticationManager authenticationManager;
	
	private IJWTService jwtService;
	
	public JWTAuthenticationFilter( AuthenticationManager authenticationManager, IJWTService jwtService) {
		this.authenticationManager=authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login","POST"));
		this.jwtService=jwtService;
		
	}
	
	@SuppressWarnings("null")
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		String username=obtainUsername(request);
		String password=obtainPassword(request);
	
		if(username !=null && password !=null) {
			logger.info("Username desde request parameter (form-data): "+username);
			logger.info("Password desde request parameter (form-data): "+password);
		}else {
			Usuario user=null;
			try {
				user=new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
				
				username=user.getUsername();
				password=user.getPassword();
				
				logger.info("Username desde request InputStream (raw): "+username);
				logger.info("Password desde request InputStream (raw): "+password);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		username=username.trim();
		UsernamePasswordAuthenticationToken authtoken= new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authtoken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String token=jwtService.create(authResult);
		
        response.addHeader(JWTServiceImpl.HEADER_STRING,JWTServiceImpl.TOKEN_PREFIX + token);
		
		Map<String, Object> body=new HashMap<String, Object>();
		body.put("token", token);
		body.put("user", (User) authResult.getPrincipal());
		body.put("mensaje", "Hola "+((User) authResult.getPrincipal()).getUsername()+" has iniciado sesión con éxito");
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("aplication/json");

	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, Object> body=new HashMap<String, Object>();
		body.put("mensaje", "Error de autenticación: Username o password incorrecto!");
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("aplication/json");
	}	
	
}
