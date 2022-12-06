package com.mongo.app.interfaceService;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mongo.app.models.Usuario;

public interface IUsuarioService {

	public ResponseEntity<Usuario> save(Usuario usuario);

	public List<Usuario> findAllUsuarios();

	public ResponseEntity<Usuario> update(Usuario usuario);

	public ResponseEntity<String> delete(String id);
	
	public Usuario findById(String id);
}
