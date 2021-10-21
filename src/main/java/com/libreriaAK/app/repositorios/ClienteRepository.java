package com.libreriaAK.app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libreriaAK.app.entidades.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
