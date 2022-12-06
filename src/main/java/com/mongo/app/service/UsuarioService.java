package com.mongo.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongo.app.interfaceService.IUsuarioService;
import com.mongo.app.models.Usuario;
import com.mongo.app.repository.UsuarioRepository;
@Service
public class UsuarioService implements IUsuarioService{

	@Autowired
	private UsuarioRepository repository;
	
	@Override
	public ResponseEntity<Usuario> save(Usuario usuario) {
		try {
			if(repository.findByNombreAndEmail(usuario.getNombre(), usuario.getEmail()).isEmpty()) {
				Usuario userSave=repository.save(usuario);
				return new ResponseEntity<Usuario> (userSave, HttpStatus.CREATED);
			}else {
				System.out.println("Usuario ya existe");
				return new ResponseEntity<Usuario> (usuario, HttpStatus.FOUND);
			}	
		}catch(Exception e){
			return new ResponseEntity<Usuario> (new Usuario(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<Usuario> findAllUsuarios() {
		try {
			return repository.findAll();
		}catch(Exception e){
			return new ArrayList<>();
		}
	}

	@Override
	public ResponseEntity<Usuario> update(Usuario usuario) {
		try {
			Usuario userUdapte=repository.save(usuario);
			return new ResponseEntity<Usuario> (userUdapte, HttpStatus.FOUND);
		}catch(Exception e){
			return new ResponseEntity<Usuario> (new Usuario(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Usuario findById(String id) {
		return repository.findById(id).orElse(new Usuario());
	}

	@Override
	public ResponseEntity<String> delete(String id) {
		try {
			Optional<Usuario> user=repository.findById(id);
			return new ResponseEntity<String> ("El usuario "+user.get().getNombre()+" fue eliminado con éxito", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String> (e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
