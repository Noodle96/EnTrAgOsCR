package com.entragos.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.entragos.springboot.app.models.entity.Producto;

public interface IProductoDao extends PagingAndSortingRepository<Producto, Long>{
	/*
	 * Nothing to do
	 * */
}
