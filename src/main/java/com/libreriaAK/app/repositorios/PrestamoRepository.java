package com.libreriaAK.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.libreriaAK.app.entidades.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, String> {

	@Query("SELECT p FROM Prestamo p WHERE p.cliente.documento = :documento")
	public Prestamo traerPrestamoPorCliente(@Param("documento") Long documento);
}
