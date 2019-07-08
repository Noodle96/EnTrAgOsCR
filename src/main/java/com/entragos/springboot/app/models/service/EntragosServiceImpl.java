package com.entragos.springboot.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.entragos.springboot.app.models.dao.IProductoDao;
import com.entragos.springboot.app.models.entity.Producto;

@Service
public class EntragosServiceImpl implements IEntragosService {

	@Autowired
	private IProductoDao productoDao;
	
	@Override
	public Page<Producto> findAll(Pageable pageable) {
		return productoDao.findAll(pageable);
	}

	@Override
	public void save(Producto producto) {
		productoDao.save(producto);
	}

	
	//retorna un producto buscado por su id
	@Override
	public Producto findOne(Long id) {
		return productoDao.findById(id).orElse(null);
	}

	@Override
	public void deleteProducto(Long id) {
		productoDao.deleteById(id);
	}

}
