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

import com.libreriaAK.app.entidades.Cliente;
import com.libreriaAK.app.exceptions.LibreriaException;
import com.libreriaAK.app.servicios.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/crear-cliente")
	public String crearCliente() {
		return "crear-cliente";
	}
	
	@PostMapping("/crear-cliente")
	public String crearCliente(Model model,@RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String domicilio, @RequestParam String telefono ) {
		try {
			clienteService.crearCliente(documento, nombre, apellido, domicilio, telefono);
			model.addAttribute("ok", true);
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "crear-cliente";
		}catch(Exception e) {
			model.addAttribute("error", e.getMessage());
			return "crear-cliente";
		}	
		return "crear-cliente";
	}
	
	@PostMapping("/guardar-cliente")
	public String guardarCliente(RedirectAttributes redirectAt,@RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String domicilio, @RequestParam String telefono ) {
		try {
			clienteService.modificarCliente(documento, nombre, apellido, domicilio, telefono);
			redirectAt.addFlashAttribute("ok", true);
		}catch(LibreriaException e) {
			redirectAt.addFlashAttribute("error", e.getMessage());
			return "redirect:/cliente/mostrar-clientes";
		}catch(Exception e) {
			redirectAt.addFlashAttribute("error", e.getMessage());
			return "redirect:/cliente/mostrar-clientes";
		}
				
		return "redirect:/cliente/mostrar-clientes";
	}
	
	@GetMapping("/mostrar-clientes")
	public String mostrarCliente(Model model){
		try{
			List<Cliente> listaClientes = clienteService.traerClientes();
			model.addAttribute("clientes", listaClientes);
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "mostrar-clientes";
		}
		return "mostrar-clientes";
	}
	
	@PostMapping("/mostrar-clientes")
	public String mostrarCliente1(Model model, @RequestParam Long documento){
		try{
			List<Cliente> clientes = new ArrayList<Cliente>();
			Cliente c = clienteService.traerCliente(documento);
			clientes.add(c);
			model.addAttribute("clientes", clientes);
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "mostrar-clientes";
		}
		return "mostrar-clientes";
	}
	
	@GetMapping("/borrar-cliente")
	public String borrarCliente(Model model, @RequestParam Long documento) {
		try {
			clienteService.eliminarCliente(documento);
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "mostrar-clientes";
		}
		return "redirect:/cliente/mostrar-clientes";
	}
	
	@GetMapping("/modificar-cliente")
	public String modificarCliente(Model model, @RequestParam Long documento) {
		try {
			Cliente c = clienteService.traerCliente(documento);
			model.addAttribute("cliente", c);
		}catch(LibreriaException e) {
			model.addAttribute("error", e.getMessage());
			return "mostrar-clientes";
		}
		return "modificar-cliente";
	}
}