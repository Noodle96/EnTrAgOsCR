package com.entragos.springboot.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entragos.springboot.app.models.dao.IProductoDao;
import com.entragos.springboot.app.models.dao.IUsuarioDao;
import com.entragos.springboot.app.models.entity.Producto;
import com.entragos.springboot.app.models.entity.Usuario;

@Service
public class EntragosServiceImpl implements IEntragosService {

	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAll(Pageable pageable) {
		return productoDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Producto producto) {
		productoDao.save(producto);
	}

	
	//retorna un producto buscado por su id
	@Override
	@Transactional(readOnly = true)
	public Producto findOne(Long id) {
		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteProducto(Long id) {
		productoDao.deleteById(id);
	}

	@Override
	@Transactional
	public void saveUsuario(Usuario usuario) {
		usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
		usuarioDao.save(usuario);
	}

}
