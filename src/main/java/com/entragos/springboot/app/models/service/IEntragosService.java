package com.entragos.springboot.app.models.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.entragos.springboot.app.models.entity.Producto;

public interface IEntragosService {
	public Page<Producto> findAll(Pageable pageable);
}