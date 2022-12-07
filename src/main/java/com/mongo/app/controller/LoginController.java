package com.mongo.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.app.models.Usuario;
import com.mongo.app.security.IJWTService;


@CrossOrigin(origins = "*")
@RestController
public class LoginController {
	
	@Autowired
	private IJWTService jwtService;
	
	@Autowired 
	private DaoAuthenticationProvider daoAuthenticationProvider;
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody Usuario usuario){
		System.out.println("TEST LOGIN");
		Authentication authenticacion=daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(usuario.getUsername(), usuario.getPassword()));
		String token;
		Map<String, Object> map=new HashMap<>();
		try {
			token = jwtService.create(authenticacion);
			System.out.println("token");
			System.out.println(token);
			map.put("token", token);
			map.put("usuario", usuario.getUsername());
//			map.put("usuario", jwtService.getUsername(token));
//			map.put("roles", jwtService.getRoles(token));
			return new ResponseEntity<Map<String, Object>> (map,HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>> (map,HttpStatus.BAD_REQUEST);
		}
		
	}
}
