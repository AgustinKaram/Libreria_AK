package com.libreriaAK.app.controladores;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.libreriaAK.app.entidades.Prestamo;
import com.libreriaAK.app.exceptions.LibreriaException;
import com.libreriaAK.app.servicios.LibroService;
import com.libreriaAK.app.servicios.PrestamoService;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {
	
	@Autowired
	private PrestamoService prestamoService;
	
	@Autowired
	private LibroService libroService;
	
	@GetMapping("/crear-prestamo")
	public String crearPrestamo(Model model) {
		return "crear-prestamo";
	}
	
	@PostMapping("/crear-prestamo")
	public String guardarPrestamo(Model model , @RequestParam String devolucion, @RequestParam String libros, @RequestParam String documento) {
		try {
			prestamoService.validarAlta(devolucion, libros, documento);
			String[] fechasSeparadas = devolucion.split("-");
			Calendar fechaDev = new GregorianCalendar();
			fechaDev.set(Integer.parseInt(fechasSeparadas[0]), Integer.parseInt(fechasSeparadas[1])-1,Integer.parseInt(fechasSeparadas[2]));
			Date fechaDevolucion = new Date(fechaDev.getTimeInMillis());
			prestamoService.crearPrestamo(fechaDevolucion,libros,Long.parseLong(documento));
			model.addAttribute("ok", "ok");
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "crear-prestamo";
		}catch(Exception e) {
			model.addAttribute("error", e.getMessage());
			return "crear-prestamo";
		}		
		return "crear-prestamo"; 
	}
	
	@GetMapping("/mostrar-prestamos")
	public String mostrarPrestamos(Model model) {
		try{
			List <Prestamo> prestamos = prestamoService.mostrarPrestamos();
			model.addAttribute("prestamos", prestamos);
			return "mostrar-prestamo";
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "mostrar-prestamo";
		}
	}
	
	@GetMapping("/mostrar-libros")
	public String mostrarLibros(Model model) {
			List <Libro> libros = libroService.mostrarLibreria();
			model.addAttribute("libros", libros);
			return "mostrar-libros";
	}

	
	@GetMapping("/enviar-libros")
	public String enviarLibros(RedirectAttributes redirectAt, @RequestParam String libros) {
		redirectAt.addFlashAttribute("libros", libros);
		return "redirect:/prestamo/crear-prestamo";
	}
	
	
	@GetMapping("/borrar-prestamo")
	public String borrarLibros(Model model, @RequestParam String id) {
		try{
			prestamoService.eliminarPrestamo(id);
			return "redirect:/prestamo/mostrar-prestamos";
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "mostrar-prestamo";
		}
	}
	
	@GetMapping("/modificar-prestamo")
	public String modificarPrestamo(Model model, @RequestParam String id) {
		try{
			Prestamo prestamo = prestamoService.mostrarPrestamo(id);
			model.addAttribute("prestamo", prestamo);
			return "modificar-prestamo";
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "mostrar-prestamo";
		}
	}	
	
	@PostMapping("/modificar-prestamo")
	public String modificarPrestamo1(RedirectAttributes redirectAt,@RequestParam String id,  @RequestParam String devolucion, @RequestParam String libros, @RequestParam(name="cliente.documento") String documento) {
		try {
			prestamoService.validarModificacion(id, devolucion, libros, documento);
			String[] fechasSeparadas = devolucion.split("-");
			Calendar fechaDev = new GregorianCalendar();
			fechaDev.set(Integer.parseInt(fechasSeparadas[0]), Integer.parseInt(fechasSeparadas[1]) - 1,Integer.parseInt(fechasSeparadas[2]));
			Date fechaDevolucion = fechaDev.getTime();
			prestamoService.modificarPrestamo(id,fechaDevolucion,libros,Long.parseLong(documento));
			redirectAt.addFlashAttribute("ok", "ok");
		}catch(LibreriaException e) {
			redirectAt.addFlashAttribute("error", e.getMessage());
			return "redirect:/prestamo/mostrar-prestamo";
		}catch(Exception e) {
			redirectAt.addFlashAttribute("error", e.getMessage());
			return "redirect:/prestamo/mostrar-prestamos";
		}
				
		return "redirect:/prestamo/mostrar-prestamos"; 
	}
}
