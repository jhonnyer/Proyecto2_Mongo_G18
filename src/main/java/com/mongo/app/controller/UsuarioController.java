package com.mongo.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.app.interfaceService.IUsuarioService;
import com.mongo.app.models.Usuario;


//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuarios")
@Secured("ROLE_ADMIN")
public class UsuarioController {
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@PostMapping(value="/save")
	public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario){
		return usuarioService.save(usuario);
	}
	
//	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping(value="/getAll")
	public List<Usuario> findAllUsuarios(){
		return usuarioService.findAllUsuarios();
	}
	
	@PutMapping(value="/update")
	public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario){
		return usuarioService.update(usuario);
	}
	
	@DeleteMapping(value="/delete/{id}")
	public ResponseEntity<String> deleteUsuario(@PathVariable("id") String id){
		System.out.println("id "+id);
		return usuarioService.delete(id);
	}
	
//	@Secured({"ROLE_USER"})
	@GetMapping(value="/findOne/{id}")
	public Usuario findAllUsuarios(@PathVariable("id") String id){
		return usuarioService.findById(id);
	}
}	
