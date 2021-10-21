package com.libreriaAK.app.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {
	
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	
	@PostMapping("/administracion")
	public String administracion(@RequestParam String contraceña) {
		if(contraceña.equals("123")){
			return "administracion.html";
		}else {
			return "acceso.html";
		}	
	}
	
	@GetMapping("/acceso")
	public String acceso() {
		return "acceso.html";
	}
	
	@GetMapping("/prestamo")
	public String prestamo() {
		return "prestamo.html";
	}
	
}
