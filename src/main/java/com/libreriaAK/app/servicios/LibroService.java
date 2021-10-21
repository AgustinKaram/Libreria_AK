package com.libreriaAK.app.servicios;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libreriaAK.app.entidades.Autor;
import com.libreriaAK.app.entidades.Editorial;
import com.libreriaAK.app.entidades.Libro;
import com.libreriaAK.app.exceptions.LibreriaException;
import com.libreriaAK.app.repositorios.AutorRepository;
import com.libreriaAK.app.repositorios.EditorialRepository;
import com.libreriaAK.app.repositorios.LibroRepository;



@Service
public class LibroService {

	@Autowired
	private AutorRepository autorRepository;
	@Autowired
	private EditorialRepository editorialRepository;
	@Autowired
	private LibroRepository libroRepository;
	
	@Transactional 
	public void crearLibro(Long isbn, String titulo, String anio, String ejemplares, String nombreAutor, String nombreEditorial) throws LibreriaException {
		try {
			validarLibro(isbn,titulo,anio,ejemplares,nombreAutor,nombreEditorial);
			Integer year = Integer.parseInt(anio);
			Integer ejemplares1 = Integer.parseInt(ejemplares);
			if(autorRepository.buscarAutorPorNombre(nombreAutor) == null && editorialRepository.buscarEditorialPorNombre(nombreEditorial) == null){
				Libro libro = new Libro();
				Autor autor = new Autor(nombreAutor);
				Editorial editorial = new Editorial(nombreEditorial);
				libro.setIsbn(isbn);
				libro.setTitulo(titulo);
				libro.setAnio(year);
				libro.setEjemplares(ejemplares1);
				libro.setPrestados(0);
				libro.setAutor(autor);
				libro.setEditorial(editorial);
				
				autorRepository.save(autor);
				editorialRepository.save(editorial);
				libroRepository.save(libro);
				
			}else if(autorRepository.buscarAutorPorNombre(nombreAutor) == null && editorialRepository.buscarEditorialPorNombre(nombreEditorial) != null) {
				Libro libro = new Libro();
				Autor autor = new Autor(nombreAutor);
				Editorial editorial = new Editorial();
				libro.setIsbn(isbn);
				libro.setTitulo(titulo);
				libro.setAnio(year);
				libro.setEjemplares(ejemplares1);
				libro.setPrestados(0);
				editorial = editorialRepository.buscarEditorialPorNombre(nombreEditorial);
				libro.setAutor(autor);
				libro.setEditorial(editorial);
				
				autorRepository.save(autor);
				libroRepository.save(libro);
				
			}else if(autorRepository.buscarAutorPorNombre(nombreAutor) != null && editorialRepository.buscarEditorialPorNombre(nombreEditorial) == null) {
				Libro libro = new Libro();
				Autor autor = new Autor();
				Editorial editorial = new Editorial(nombreEditorial);
				libro.setIsbn(isbn);
				libro.setTitulo(titulo);
				libro.setAnio(year);
				libro.setEjemplares(ejemplares1);
				libro.setPrestados(0);
				autor = autorRepository.buscarAutorPorNombre(nombreAutor);
				libro.setAutor(autor);
				libro.setEditorial(editorial);
				
				editorialRepository.save(editorial);
				libroRepository.save(libro);
				
			}else {
				Libro libro = new Libro();
				Autor autor = new Autor();
				Editorial editorial = new Editorial();
				libro.setIsbn(isbn);
				libro.setTitulo(titulo);
				libro.setAnio(year);
				libro.setEjemplares(ejemplares1);
				libro.setPrestados(0);
				autor = autorRepository.buscarAutorPorNombre(nombreAutor);
				editorial = editorialRepository.buscarEditorialPorNombre(nombreEditorial);
				libro.setAutor(autor);
				libro.setEditorial(editorial);
				
				libroRepository.save(libro);
			}
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new LibreriaException("Error de sistema");
		}
	}
	
	@Transactional 
	public void modificarLibro (Long isbn, String titulo, String anio, String ejemplares, String nombreAutor, String nombreEditorial) throws LibreriaException{
		try {
			validarLibro(isbn, titulo, anio, ejemplares, nombreAutor, nombreEditorial);
			Integer year = Integer.parseInt(anio);
			Integer ejemplares1 = Integer.parseInt(ejemplares);
			Optional<Libro> respuesta = libroRepository.findById(isbn);
			if(respuesta.isPresent()) {
				Libro libro = respuesta.get();
				Autor autor = new Autor();
				Editorial editorial = new Editorial();
				libro.setTitulo(titulo);
				libro.setAnio(year);
				libro.setEjemplares(ejemplares1);
				autor = autorRepository.buscarAutorPorNombre(nombreAutor);
				editorial = editorialRepository.buscarEditorialPorNombre(nombreEditorial);
				libro.setAutor(autor);
				libro.setEditorial(editorial);
				
				libroRepository.save(libro);
			}else {
				throw new LibreriaException("El libro a modificar no se encuentra");
			}
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new LibreriaException("Error de sistema");
		}		
	}
	
	@Transactional 
	public void prestarLibro(Long isbn) throws LibreriaException{
		try {
			Optional<Libro> respuesta = libroRepository.findById(isbn);
			if(respuesta.isPresent()) {
				Libro libro = respuesta.get();
				if(libro.getPrestados() < libro.getEjemplares()) {
					Integer prestamo = libro.getPrestados() + 1;
					libro.setPrestados(prestamo);
				}else {
					throw new LibreriaException("No hay disponibilidad para el libro solicitado.");
				}
				
				libroRepository.save(libro);
			}else {
				throw new LibreriaException("El libro a prestar no se encuentra");
			}
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new LibreriaException("Error de sistema");
		}
	}
	
	@Transactional 
	public void desPrestarLibro(List<Libro> libros) throws LibreriaException{
			for(Libro l : libros) {
				l.setPrestados(l.getPrestados()-1);
				libroRepository.save(l);
			}
	}
	
	@Transactional 
	public void EliminarLibro(Long isbn) throws LibreriaException{
		try {
			Optional<Libro> respuesta = libroRepository.findById(isbn);
			if(respuesta.isPresent()) {
				Libro libro = respuesta.get();
				libroRepository.delete(libro);
			}else {
				throw new LibreriaException("El libro a eliminar no se encuentra");
			}
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new LibreriaException("Error de sistema");
		}
	}
	
	public List<Libro> mostrarLibreria(){
		return libroRepository.findAll();
	}
	
	public Libro buscarLibro(Long isbn) throws LibreriaException{
		try {
			Optional<Libro> respuesta = libroRepository.findById(isbn);
			if(respuesta.isPresent()) {
				return respuesta.get();
			}else {
				throw new LibreriaException("No hay un libro asociado a ese codigo isbn."); 	
			}
		}catch(LibreriaException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new LibreriaException("Error de sistema");
		}	
	}
	
	public void validarLibro(Long isbn, String titulo, String anio, String ejemplares, String nombreAutor, String nombreEditorial) throws LibreriaException{
		if(isbn == null || isbn <= 13) {
			throw new LibreriaException("Codigo isbn vacio o incorrecto");
		}
		if(titulo.isEmpty() || titulo == null) {
			throw new LibreriaException("Titulo vacio");
		}
		if(anio.isEmpty() || anio == null) {
			throw new LibreriaException("Año vacio o incorrecto");
		}else {
			try {
				Integer.parseInt(anio);
			}catch(NumberFormatException ex) {
				throw new LibreriaException("Ingreso un caracter alfabético en el campo 'Año'.");
			}
		}
		if(ejemplares.isEmpty() || ejemplares == null) {
			throw new LibreriaException("Ejemplares vacio o incorrecto");
		}else {
			try {
				Integer.parseInt(ejemplares);
			}catch(NumberFormatException ex) {
				throw new LibreriaException("Ingreso un caracter alfabético en el campo 'Ejemplares'.");
			}
		}
		if(nombreAutor.isEmpty() || nombreAutor == null) {
			throw new LibreriaException("Nombre autor vacio");
		}
		if(nombreEditorial.isEmpty() || nombreEditorial == null) {
			throw new LibreriaException("Editorial autor vacio");
		}
	}

}
