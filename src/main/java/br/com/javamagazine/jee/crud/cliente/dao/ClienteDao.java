package br.com.javamagazine.jee.crud.cliente.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;

import br.com.javamagazine.jee.crud.cliente.entity.Cliente;
import br.com.javamagazine.jee.crud.cliente.entity.filter.ClienteFilter;

@Stateless
public class ClienteDao {
	
	@PersistenceContext
	private EntityManager em;
	
	public Cliente save(Cliente cliente) {
		if (cliente.getId() == null) {
			em.persist(cliente);
		} else {
			em.merge(cliente);
		}
		return cliente;
	}
	
	public void remove(Long id) {
		em.createQuery("DELETE FROM Cliente c WHERE c.id = :id").setParameter("id", id).executeUpdate();
	}
	
	public Cliente findById(Long id) {
		return em.find(Cliente.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> findByFilter(ClienteFilter filter) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT c FROM Cliente c");
		
		if (filter != null && filter.getNome() != null) {
			jpql.append(" WHERE c.nome like :nome");
		}
		
		jpql.append(" ORDER BY c.nome");
		Query query = em.createQuery(jpql.toString());
		
		if (filter != null && filter.getNome() != null) {
			query.setParameter("nome", "%" + filter.getNome() + "%");
		}	
		
		return query.getResultList();
	}
	
	public boolean existe(Cliente cliente) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(c) FROM Cliente c WHERE c.email = :email");
		
		if (cliente.getId() != null) {
			jpql.append(" AND c.id != :id");
		}
		
		Query query = em.createQuery(jpql.toString());
		query.setParameter("email", cliente.getEmail());
		
		if (cliente.getId() != null) {
			query.setParameter("id", cliente.getId());
		}
		
		return ((Long) query.getSingleResult()).intValue() > 0;
	}

}
