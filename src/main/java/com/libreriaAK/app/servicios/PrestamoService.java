package com.libreriaAK.app.servicios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libreriaAK.app.entidades.Cliente;
import com.libreriaAK.app.entidades.Libro;
import com.libreriaAK.app.entidades.Prestamo;
import com.libreriaAK.app.exceptions.LibreriaException;
import com.libreriaAK.app.repositorios.ClienteRepository;
import com.libreriaAK.app.repositorios.LibroRepository;
import com.libreriaAK.app.repositorios.PrestamoRepository;

@Service
public class PrestamoService {
	
	@Autowired
	PrestamoRepository prestamoRepository;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	LibroRepository libroRepository;
	
	@Autowired
	LibroService libroService;
	
	@Transactional
	public void crearPrestamo (Date devolucion, String libros, Long documento) throws LibreriaException{
		try {
			Prestamo prestamo = new Prestamo();
			Optional<Cliente> c = clienteRepository.findById(documento);
			if(c.isPresent()) {
				prestamo.setFecha();
				prestamo.setDevolucion(devolucion);
				/* 
				 Estos codigos son para buscar todos los libros que el cliente desea pedir prestado
				 y pasarselos a la entidad prestamo la lista de libros que desea pedir prestado.
				 */
				String[] listaLibros = libros.split(",");
				List<Long> listaLibrosL = new ArrayList<Long>();
				for(int i = 0 ; i < listaLibros.length ; i++) {
					listaLibrosL.add(Long.parseLong(listaLibros[i]));
				}	
				if(listaLibros.length == 1) {
					Optional<Libro> l = libroRepository.findById(Long.parseLong(listaLibros[0]));
					if(l.isPresent()) {
						List<Libro> li = new ArrayList<Libro>();
						libroService.prestarLibro(l.get().getIsbn());
						li.add(l.get());
						Optional<Libro> l2 = libroRepository.findById(1234567891234L);
						if(l2.isPresent()) {
							libroService.prestarLibro(l2.get().getIsbn());
							li.add(l2.get());
						}
						prestamo.setLibros(li);
					}else {
						throw new LibreriaException("Libro no disponible o no encontrado.");
					}
				}else if (listaLibros.length == 2) {
					Iterable<Libro> listaLibrosAprestar = libroRepository.findAllById(listaLibrosL);
					List<Libro> li = new ArrayList<Libro>();
					for(Libro l : listaLibrosAprestar) {
						libroService.prestarLibro(l.getIsbn());
						li.add(l);
					}
					prestamo.setLibros(li);
				}else {
					throw new LibreriaException("No se puede prestar mas de dos libros.");
				}
				// aca termina de setear los libros
				prestamo.setCliente(c.get());
			}else {
				throw new LibreriaException("Cliente no registrado");
			}
			prestamoRepository.save(prestamo);
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			throw new LibreriaException("Error de sistema");
		}
	}
	
	public List<Prestamo> mostrarPrestamos() throws LibreriaException {
		try{
			List<Prestamo> li = prestamoRepository.findAll();
			return li;
		}catch(Exception e) {
			throw new LibreriaException("No hay prestamos.");
		}
	}
	
	public Prestamo mostrarPrestamo(String id) throws LibreriaException {
			Optional<Prestamo> p = prestamoRepository.findById(id);
			if(p.isPresent()) {
				Prestamo pres = p.get();
				return pres;
			}else {
				throw new LibreriaException("No hay prestamo vinculado a ese id.");
			}
	}
	
	@Transactional
	public void eliminarPrestamo(String id) throws LibreriaException {
			Optional<Prestamo> pre = prestamoRepository.findById(id);
			if(pre.isPresent()) {
				Prestamo p = pre.get();
				libroService.desPrestarLibro(p.getLibros());
				prestamoRepository.delete(p);
			}else {
				throw new LibreriaException("No se encuentra el prestamo a eliminar.");
			}
	}
	
	@Transactional
	public void modificarPrestamo(String id, Date devolucion, String libros, Long documento) throws LibreriaException {
		try {
			Prestamo prestamo = null;
			Optional<Prestamo> optionPrestamo = prestamoRepository.findById(id);
			if(optionPrestamo.isPresent()) {
				prestamo = optionPrestamo.get();
				/* 
				 Si existe el prestamo a modificar, primero vamos a quitar el prestamo de los libros que se han prestados
				 para luego volver a dejar registrado los prestamos.
				 */
				libroService.desPrestarLibro(prestamo.getLibros());
			}else {
				throw new LibreriaException("No existe el prestamo a modificar.");
			}
			//evaluamos si existe el cliente
			Optional<Cliente> c = clienteRepository.findById(documento);
			if(c.isPresent()) {
				prestamo.setCliente(c.get());
				prestamo.setDevolucion(devolucion);
				/* 
				 Estos codigos son para buscar todos los libros que el cliente desea pedir prestado
				 y pasarselos a la entidad prestamo la lista de libros que desea pedir prestado.
				 */
				String[] listaLibros = libros.split(",");
				List<Long> listaLibrosL = new ArrayList<Long>();
				for(int i = 0 ; i < listaLibros.length ; i++) {
					listaLibrosL.add(Long.parseLong(listaLibros[i]));
				}	
				if(listaLibros.length == 1) {
					Optional<Libro> l = libroRepository.findById(Long.parseLong(listaLibros[0]));
					if(l.isPresent()) {
						List<Libro> li = new ArrayList<Libro>();
						libroService.prestarLibro(l.get().getIsbn());
						li.add(l.get());
						prestamo.setLibros(li);
					}else {
						throw new LibreriaException("Libro no disponible o no encontrado.");
					}
				}else if (listaLibros.length == 2) {
					Iterable<Libro> listaLibrosAprestar = libroRepository.findAllById(listaLibrosL);
					List<Libro> li = new ArrayList<Libro>();
					for(Libro l : listaLibrosAprestar) {
						libroService.prestarLibro(l.getIsbn());
						li.add(l);
					}
					prestamo.setLibros(li);
				}else {
					throw new LibreriaException("No se puede prestar mas de dos libros.");
				}
				// aca termina de setear los libros
			}else {
				throw new LibreriaException("Cliente no registrado");
			}
			prestamoRepository.save(prestamo);
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			throw new LibreriaException("Error de sistema");
		}
	}
	
	public void validarModificacion(String id, String devolucion, String libros, String documento) throws LibreriaException{
		
		if(devolucion == null || devolucion.isEmpty()) {
			throw new LibreriaException("Fecha de devolucion inválida o vacia.");
		}else {
			Prestamo prestamoAux = mostrarPrestamo(id);
			String[] fechasSeparada = devolucion.split("-");
			Calendar fechaDev = new GregorianCalendar();
			fechaDev.set(Integer.parseInt(fechasSeparada[0]), Integer.parseInt(fechasSeparada[1]) - 1,Integer.parseInt(fechasSeparada[2]));
			Date fechaDevolucion = fechaDev.getTime();
			if(prestamoAux.getFecha().after(fechaDevolucion)) {
				throw new LibreriaException("No puede elegir una fecha de devolucion anterior a la fecha de alta del prestamo.");
			}
		}
		if(libros == null || libros.isEmpty()) {
			throw new LibreriaException("Campo isbn vacio.");
		}
		if(documento == null || libros.isEmpty()) {
			throw new LibreriaException("Cliente invalido o inexistente.");
		}else {
			try {
				Long.parseLong(documento);
			}catch(NumberFormatException ex) {
				throw new LibreriaException("Ingreso un caracter alfabético en el campo 'DNI'.");
			}
		}
	}
	
	public void validarAlta (String devolucion, String libros, String documento) throws LibreriaException{
		
		if(devolucion == null || devolucion.isEmpty()) {
			throw new LibreriaException("Fecha de devolucion inválida o vacia.");
		}else {
			Date hoy = new Date();
			String[] fechasSeparada = devolucion.split("-");
			Calendar fechaDev = new GregorianCalendar();
			fechaDev.set(Integer.parseInt(fechasSeparada[0]), Integer.parseInt(fechasSeparada[1]) - 1,Integer.parseInt(fechasSeparada[2]));
			Date fechaDevolucion = fechaDev.getTime();
			if(hoy.after(fechaDevolucion)) {
				throw new LibreriaException("No puede elegir una fecha de devolucion anterior a la fecha de alta del prestamo.");
			}
		}
		if(libros == null || libros.isEmpty()) {
			throw new LibreriaException("Campo isbn vacio.");
		}
		if(documento == null || documento.isEmpty()) {
			throw new LibreriaException("Cliente invalido o inexistente.");
		}else {
			try {
				Long.parseLong(documento);
			}catch(NumberFormatException ex) {
				throw new LibreriaException("Ingreso un caracter alfabético en el campo 'DNI'.");
			}
		}
	}

}
