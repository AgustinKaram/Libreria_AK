package com.libreriaAK.app.servicios;

import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libreriaAK.app.entidades.Cliente;
import com.libreriaAK.app.exceptions.LibreriaException;
import com.libreriaAK.app.repositorios.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Transactional
	public void crearCliente(Long documento, String nombre, String apellido, String domicilio, String telefono) throws LibreriaException{
		try {
			validarCliente(documento, nombre, apellido, domicilio, telefono);
			Cliente cliente = new Cliente();
			cliente.setDocumento(documento);
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setDomicilio(domicilio);
			cliente.setTelefono(telefono);
			
			clienteRepository.save(cliente);
			
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new LibreriaException("Error de sistema.");
		}
		
	}
	
	@Transactional
	public void modificarCliente(Long documento, String nombre, String apellido, String domicilio, String telefono) throws LibreriaException{
		try {
			validarCliente(documento, nombre, apellido, domicilio, telefono);
			Optional<Cliente> cliente = clienteRepository.findById(documento);
			if(cliente.isPresent()) {
				Cliente c = cliente.get();
				c.setDocumento(documento);
				c.setNombre(nombre);
				c.setApellido(apellido);
				c.setDomicilio(domicilio);
				c.setTelefono(telefono);
				
				clienteRepository.save(c);
			}else {
				throw new LibreriaException("No se encuentra ese cliente.");
			}
			
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new LibreriaException("Error de sistema.");
		}
		
	}
	
	public List<Cliente> traerClientes() throws LibreriaException{
		try {
			List<Cliente> listaClientes = clienteRepository.findAll();
			return listaClientes;
		}catch(Exception e) {
			throw new LibreriaException("Error de sistema.");
		}
	}
	
	public Cliente traerCliente(Long documento) throws LibreriaException{
		try {
			 Optional<Cliente> cli = clienteRepository.findById(documento);
			 if(cli.isPresent()) {
				 return cli.get();
			 }else {
				 throw new LibreriaException("Libro no encontrado");
			 }	
		}catch(Exception e) {
			throw new LibreriaException("Error de sistema.");
		}
	}
	
	@Transactional
	public void eliminarCliente(Long documento) throws LibreriaException, ConstraintViolationException{
		try {
			Optional<Cliente> cliente = clienteRepository.findById(documento);
			if(cliente.isPresent()) {
				Cliente c = cliente.get();
				clienteRepository.delete(c);
			}else {
				throw new LibreriaException("No se encuentra ese cliente.");
			}			
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			throw new LibreriaException("Error de sistema.");
		}	
		
	}
	
	
	private void validarCliente(Long documento, String nombre, String apellido, String domicilio, String telefono) throws LibreriaException{
		try {
			if(documento == null || documento < 7) {
				throw new LibreriaException("El documento ingresado es inválido.");
			}
			if(nombre == null || nombre.isEmpty()) {
				throw new LibreriaException("El nombre ingresado es inválido.");
			}
			if(apellido == null || apellido.isEmpty()) {
				throw new LibreriaException("El apellido ingresado es inválido.");
			}
			if(domicilio == null || domicilio.isEmpty()) {
				throw new LibreriaException("El domicilio ingresado es inválido.");
			}
			if(telefono == null || telefono.isEmpty()) {
				throw new LibreriaException("El telefono ingresado es inválido.");
			}
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new LibreriaException("Error de sistema.");
		}
	}

}
