package com.libreriaAK.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.libreriaAK.app.entidades.Editorial;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, String>{
	
	@Query("SELECT a FROM Editorial a WHERE a.nombre = :nombre")
	public Editorial buscarEditorialPorNombre(@Param("nombre") String nombre);

}