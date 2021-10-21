package com.libreriaAK.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libreriaAK.app.entidades.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

}
