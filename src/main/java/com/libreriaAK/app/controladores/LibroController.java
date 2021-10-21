package com.libreriaAK.app.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.libreriaAK.app.entidades.Libro;
import com.libreriaAK.app.exceptions.LibreriaException;
import com.libreriaAK.app.servicios.LibroService;

@Controller
@RequestMapping("/libro")
public class LibroController {
	
	@Autowired
	private LibroService libroService;
	
	@GetMapping("/crear-libro")
	public String crearLibro() {
		return "crear-libro";
	}
	
	@PostMapping("/guardar-libro")
	public String persistirLibro(Model model, @RequestParam() Long isbn, @RequestParam() String titulo, @RequestParam() String anio, @RequestParam() String ejemplares, @RequestParam() String nombreAutor, @RequestParam() String nombreEditorial ){
		try {
			libroService.crearLibro(isbn, titulo, anio, ejemplares, nombreAutor, nombreEditorial);
			model.addAttribute("ok", "ok");
		}catch(LibreriaException e) {
				model.addAttribute("error", e.getMessage());
				return "crear-libro";
		}catch(Exception e) {
			model.addAttribute("error", e.getMessage());
			return "crear-libro";
		}
		return "crear-libro";
	}
	
	@GetMapping("/editar_eliminar-libro")
	public String mostrarLibros(Model model) {
		List<Libro> libros = libroService.mostrarLibreria(); 
		model.addAttribute("libros", libros);
		return "editar_eliminar-libro";	
	}
	
	@GetMapping("/borrar-libro")
	public String eliminar(RedirectAttributes redirectAt,@RequestParam() Long isbn) {
		try {
			libroService.EliminarLibro(isbn);
		}catch(LibreriaException e) {
			redirectAt.addFlashAttribute("error", e.getMessage());
		}catch(Exception e) {
			redirectAt.addFlashAttribute("error", e.getMessage());
		}
		if(libroService.mostrarLibreria().isEmpty()) {
			return "administracion";
		}else{
			return "redirect:/libro/editar_eliminar-libro";
		}
	}
	
	@GetMapping("/modificar-libro")
	public String modificarLibro(Model model,@RequestParam() Long isbn) {
		try {
			model.addAttribute("libro",libroService.buscarLibro(isbn));
		}catch(LibreriaException e) {
			model.addAttribute("error", e);
		}catch(Exception e) {
			model.addAttribute("error", e);
		}
		return "modificar-libro";
	}
	
	@PostMapping("/modificarlibro")
	public String modificarLibro2(RedirectAttributes redirectAt,@RequestParam() Long isbn, @RequestParam() String titulo, @RequestParam() String anio, @RequestParam() String ejemplares, @RequestParam(name = "autor.nombre") String nombreAutor, @RequestParam(name = "editorial.nombre") String nombreEditorial) {
		try {
			libroService.modificarLibro(isbn, titulo, anio, ejemplares, nombreAutor, nombreEditorial);
			redirectAt.addFlashAttribute("ok", "ok");
		}catch(LibreriaException e) {
				redirectAt.addFlashAttribute("error", e.getMessage());
				return "redirect:/libro/editar_eliminar-libro";
		}catch(Exception e) {
			redirectAt.addFlashAttribute("error", e.getMessage());
			return "redirect:/libro/editar_eliminar-libro";
		}
		return "redirect:/libro/editar_eliminar-libro";
	}
	
	
	@PostMapping("/editar_eliminar-libro")
	public String mostrarLibros(Model model, @RequestParam() Long isbn) {
		try {
			Libro libro = libroService.buscarLibro(isbn);
			List<Libro> libros = new ArrayList<>();
			libros.add(0, libro);
			model.addAttribute("libros", libros);
		}catch(LibreriaException e){
			System.out.println("asdad");
			model.addAttribute("error", e.getMessage());
		}
		return "editar_eliminar-libro";	
	}
	
}
