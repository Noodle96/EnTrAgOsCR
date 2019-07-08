package com.entragos.springboot.app.controllers;

//import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.entragos.springboot.app.models.entity.Producto;
//service general
import com.entragos.springboot.app.models.service.IEntragosService;
import com.entragos.springboot.app.utils.paginator.PageRender;


@Controller // marcar la clase como controlador
@SessionAttributes("producto")
public class ProductoController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IEntragosService entragosService;
	
	// mostrar el listado de los clientes
	@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET) // ir a esa vista listar.jsp
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model
			//Authentication authentication, 
			//HttpServletRequest request
			) {
		
		/*
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			log.info("Bienvenido Usuario con SecurityContextHolder.getContext().getAuthentication(); : ".concat(auth.getName()));
		}
		*/
		//De la forma con SecurityContextHolderAwareRequestWrapper
		
		/*
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if(securityContext.isUserInRole("ADMIN")) {
			log.info("hola Usuario forma SecurityContextHolderAwareRequestWrapper ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else {
			log.info("hola Usuario forma SecurityContextHolderAwareRequestWrapper ".concat(auth.getName()).concat(" No tienes acceso"));
		}
		*/
		
		/*
		//De la forma con request
		if(request.isUserInRole("ROLE_ADMIN")) {
			log.info("hola Usuario forma HttpServletRequest ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else {
			log.info("hola Usuario forma HttpServletRequest ".concat(auth.getName()).concat(" No tienes acceso"));
		}
		*/
		
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Producto> productos = entragosService.findAll(pageRequest);
		PageRender<Producto> pageRender = new PageRender<Producto>("/listar", productos);
		model.addAttribute("titulo", "Listado de Productos");
		model.addAttribute("Productos", productos);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	
	

}
