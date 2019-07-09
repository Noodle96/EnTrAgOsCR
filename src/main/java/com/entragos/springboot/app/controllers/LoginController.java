package com.entragos.springboot.app.controllers;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.entragos.springboot.app.models.entity.Role;
import com.entragos.springboot.app.models.entity.Usuario;
import com.entragos.springboot.app.models.service.IEntragosService;

@Controller
@SessionAttributes("usuario")
public class LoginController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	
	@Autowired
	private IEntragosService entragosService;
	
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required=false) String logout,
			Model model, Principal principal,
			RedirectAttributes flash) {
		//para evitar que haga doble inicio de sesion
		if(principal !=  null) {
			flash.addFlashAttribute("info", "Ya ha iniciado sesion anteriormete");
			return "redirect:/";
		}
		if(error != null) {
			model.addAttribute("error", "Error al login: Nombre o password Incorrect, Try Again!!");
			
		}
		if(logout != null) {
			model.addAttribute("success", "Ha cerrado sesión con éxito !!");
		}
		return "login";
	}
	
	
	@RequestMapping(value = "/SignUp", method = RequestMethod.GET) // get
	public String crear(Map<String, Object> model) {
		Usuario usuario = new Usuario();
		model.put("usuario", usuario);
		model.put("titulo", "Sign Up Usuario CR");
		return "formSingUp";
	}
	
	
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST) // post
	public String guardar(@Valid Usuario usuario, BindingResult result, Model mode,
			 RedirectAttributes flash, SessionStatus status) {
		
		if (result.hasErrors()) {
			mode.addAttribute("titulo", "formulario Usuario errors");
			return "formSingUp";
		}
		
		//String mensajeFlash = (producto.getId() != null) ? "Cliente editado con éxito!" + producto.getNombre()
		//		: "Cliente creado con éxito!" + producto.getNombre();
		//log.info("ANTES DE SAVE: "+producto.toString());
		
		Role role = new Role();
		role.setAuthority("ROLE_USER");
		usuario.addRole(role);
		
		log.info("ANTES INFO USUARIO: " + usuario.toString());
		entragosService.saveUsuario(usuario);
		log.info("DESPUES INFO USUARIO: " + usuario.toString());
		status.setComplete();
		
		//log.info("DESPUES DE SAVE: " +producto.toString());
		//flash.addFlashAttribute("success", mensajeFlash);
		flash.addFlashAttribute("success", "Usuario " + usuario.getUsername() +" Creado Con éxito");
		return "redirect:listar";
	}
	
	
	
	
}
