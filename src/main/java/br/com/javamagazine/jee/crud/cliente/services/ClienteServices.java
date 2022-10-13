package br.com.javamagazine.jee.crud.cliente.services;

import java.util.List;
import javax.ejb.Local;

import br.com.javamagazine.jee.crud.cliente.entity.Cliente;
import br.com.javamagazine.jee.crud.cliente.entity.filter.ClienteFilter;
import br.com.javamagazine.jee.crud.cliente.exception.*;

@Local
public interface ClienteServices {
	
	Cliente save(Cliente cliente) throws ClienteExistenteException, NenhumTelefoneInformadoException;
	
	void remove(Long id);
	
	Cliente findById(Long id);
	
	List<Cliente> findByFilter(ClienteFilter filter);
}
