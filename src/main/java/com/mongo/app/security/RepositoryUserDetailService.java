package com.mongo.app.security;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongo.app.models.Usuario;
import com.mongo.app.repository.UsuarioRepository;

@Service
public class RepositoryUserDetailService implements UserDetailsService {
	
	private Logger logger=LoggerFactory.getLogger(RepositoryUserDetailService.class);

	@Autowired
	private UsuarioRepository usuarioDao;
	
	@SuppressWarnings("unused")
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario=usuarioDao.findByUsername(username);
		
		logger.info(username);
		logger.info(usuario.getUsername());
		logger.info(usuario.getPassword());
		
		if(usuario == null) {
			logger.error("Error en el login, el usuario "+username+" no existe en el sistema" );
			throw new UsernameNotFoundException("Error en el login, el usuario "+username+" no existe en el sistema");
		}
		
		List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		
		for(String role: usuario.getRoles()) {
			logger.info("Role: "+role);
			authorities.add(new SimpleGrantedAuthority(role));
		}
		
		
		if(authorities.isEmpty()) {
			logger.error("Error en el login, el usuario "+username+" no tiene roles asignados en el sistema" );
			throw new UsernameNotFoundException("Error en el login, el usuario "+username+" no tiene roles asignados en el sistema");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

}
