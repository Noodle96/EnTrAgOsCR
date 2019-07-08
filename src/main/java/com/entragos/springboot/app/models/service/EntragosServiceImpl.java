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

}
