package com.mongo.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mongo.app.models.Usuario;
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String>{
	@Query("{nombre:?0, email:?1}")
	List<Usuario> findByNombreAndEmail(String username, String email);
	
	Usuario findByUsername(String username);
}
