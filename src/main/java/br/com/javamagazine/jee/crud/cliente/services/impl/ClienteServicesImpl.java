package br.com.javamagazine.jee.crud.cliente.services.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.javamagazine.jee.crud.cliente.dao.ClienteDao;
import br.com.javamagazine.jee.crud.cliente.entity.Cliente;
import br.com.javamagazine.jee.crud.cliente.entity.filter.ClienteFilter;
import br.com.javamagazine.jee.crud.cliente.exception.*;
import br.com.javamagazine.jee.crud.cliente.services.ClienteServices;

@Stateless
public class ClienteServicesImpl implements ClienteServices {
	
	@Inject
	private ClienteDao clienteDao;
	
	@Override
	public Cliente save(Cliente cliente) throws ClienteExistenteException, NenhumTelefoneInformadoException {
		if (clienteDao.existe(cliente)) { 
			throw new ClienteExistenteException(); 
		}
		
		if (cliente.getTelefones().size() == 0) {
			throw new NenhumTelefoneInformadoException(); 
		}
	
		return clienteDao.save(cliente);
	}
	
	@Override
	public void remove(Long id) {
		clienteDao.remove(id);
	}
	
	@Override
	public Cliente findById(Long id) {
		return clienteDao.findById(id);
	}

	@Override
	public List<Cliente> findByFilter(ClienteFilter filter) {
		return clienteDao.findByFilter(filter);
	}
}
