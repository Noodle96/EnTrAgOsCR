package com.entragos.springboot.app.models.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.entragos.springboot.app.models.entity.Producto;
import com.entragos.springboot.app.models.entity.Usuario;

public interface IEntragosService {
	public Page<Producto> findAll(Pageable pageable);
	public void save(Producto producto);
	public Producto findOne(Long id);
	public void deleteProducto(Long id);
	
	public void saveUsuario(Usuario usuario);
}
