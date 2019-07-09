package com.entragos.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.entragos.springboot.app.models.entity.Producto;
//service general
import com.entragos.springboot.app.models.service.IEntragosService;
import com.entragos.springboot.app.models.service.IUploadFileService;
import com.entragos.springboot.app.utils.paginator.PageRender;



@Controller // marcar la clase como controlador
@SessionAttributes("producto")
public class ProductoController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IEntragosService entragosService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	
	
	
	
	//@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "atachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	
	
	// mostrar el listado de los clientes
	@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET) // ir a esa vista listar.jsp
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			//Authentication authentication, 
			HttpServletRequest request
			) {
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		/*
		if(auth != null) {
			log.info("Bienvenido Usuario con SecurityContextHolder.getContext().getAuthentication(); : ".concat(auth.getName()));
		}
		*/
		//De la forma con SecurityContextHolderAwareRequestWrapper
		
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if(securityContext.isUserInRole("ADMIN")) {
			log.info("hola Usuario forma SecurityContextHolderAwareRequestWrapper ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else {
			log.info("hola Usuario forma SecurityContextHolderAwareRequestWrapper ".concat(auth.getName()).concat(" No tienes acceso"));
		}
		
		
		/*
		//De la forma con request
		if(request.isUserInRole("ROLE_ADMIN")) {
			log.info("hola Usuario forma HttpServletRequest ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else {
			log.info("hola Usuario forma HttpServletRequest ".concat(auth.getName()).concat(" No tienes acceso"));
		}
		*/
		
		Pageable pageRequest = PageRequest.of(page,3 );
		Page<Producto> productos = entragosService.findAll(pageRequest);
		PageRender<Producto> pageRender = new PageRender<Producto>("/listar", productos);
		model.addAttribute("titulo", "Listado de Productos");
		model.addAttribute("Productos", productos);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	
	
	// manda el objecto cliente al form.html
		@Secured("ROLE_ADMIN")
		@RequestMapping(value = "/form", method = RequestMethod.GET) // get
		public String crear(Map<String, Object> model) {
			Producto producto = new Producto();
			model.put("producto", producto);
			model.put("titulo", "formulario Producto");
			return "formProducto";
		}
	
		
		
		
		// guardar a un cliente a la base de datos
		// @valid habilita la validacion en el objeto mapeado al form
		// SessionStatus status
		@Secured("ROLE_ADMIN")
		@RequestMapping(value = "/form", method = RequestMethod.POST) // post
		public String guardar(@Valid Producto producto, BindingResult result, Model mode,
				@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
			
			if (result.hasErrors()) {
				mode.addAttribute("titulo", "formulario Clientes errors");
				return "formProducto";
			}
			log.info("12345");
			
			// log.info("INIT GUARDAR: " + cliente.toString());
			if (!foto.isEmpty()) {
				// para edicion podemos editar la foto , entonces eliminamos la que ya no sirv
				/*
				 * cliente.getId() > 0 && cliente.getFoto() != null &&
				 * cliente.getFoto().length() > 0
				 */
				if (producto.getId() != null && producto.getFoto() != null && producto.getId() > 0
						&& producto.getFoto().length() > 0) {
					uploadFileService.delete(producto.getFoto());
				}

				// Path directorioRecursos = Paths.get("src//main//resources//static/uploads");
				// String rootPath = directorioRecursos.toFile().getAbsolutePath();
				// String rootPath = "//home//russel//uploads";

				String uniqueFilename = null;
				try {
					uniqueFilename = uploadFileService.copy(foto);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				flash.addFlashAttribute("info",
						"Has subido correctamente ' " + uniqueFilename + "'" + " para " + producto.getNombre());
				log.info("ANTES SET FOTO: " + producto.toString());
				producto.setFoto(uniqueFilename);
				log.info("DESPUES SET FOTO: " + producto.toString());
			}

			String mensajeFlash = (producto.getId() != null) ? "Cliente editado con éxito!" + producto.getNombre()
					: "Cliente creado con éxito!" + producto.getNombre();
			log.info("ANTES DE SAVE: "+producto.toString());
			entragosService.save(producto);
			status.setComplete();
			log.info("DESPUES DE SAVE: " +producto.toString());
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:listar";
		}
	
		
		
		
		@Secured("ROLE_ADMIN")
		@RequestMapping(value = "/form/{id}")
		public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
			Producto producto = null;
			if (id > 0) {
				producto = entragosService.findOne(id);
				if (producto == null) {
					flash.addFlashAttribute("error", "El ID del Producto no existe en la BBDD!");
					return "redirect:/listar";
				}
			} else {
				flash.addFlashAttribute("error", "El ID del Producto no puede ser cero!");
				return "redirect:/listar";
			}
			log.info("FINDONE: " + producto.toString());
			model.put("producto", producto);
			model.put("titulo", "Editar Productos");
			return "formProducto";
		}
		
		
		
		
		
		@Secured("ROLE_ADMIN")
		@RequestMapping(value = "/eliminar/{id}")
		public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
			if (id > 0) {
				Producto producto = entragosService.findOne(id);
				entragosService.deleteProducto(id);
				flash.addFlashAttribute("success", "Producto eliminado con éxito!");

				if(producto.getFoto() != null && producto.getFoto().length() > 0) {
					if (uploadFileService.delete(producto.getFoto())) {
						flash.addFlashAttribute("info", "foto " + producto.getFoto() + " delete succesfully");
					}
				}
			}
			return "redirect:/listar";
		}
		
		
		@Secured("ROLE_USER")
		@GetMapping(value = "/ver/{id}")
		public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
			Producto producto = entragosService.findOne(id);
			if (producto == null) {
				flash.addFlashAttribute("error", "El Producto no existe en la BBDD");
				return "redirect:/listar";
			}
			model.put("producto", producto);
			model.put("titulo", "Detalle Producto " + producto.getNombre());
			return "verDetalleProducto";
		}
	
		
		
	

}
