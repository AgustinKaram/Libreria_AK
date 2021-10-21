package com.libreriaAK.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libreriaAK.app.entidades.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, String> {
	
}
