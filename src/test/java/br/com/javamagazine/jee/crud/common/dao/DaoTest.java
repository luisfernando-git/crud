package br.com.javamagazine.jee.crud.common.dao;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class DaoTest {

    @PersistenceContext
    private EntityManager em;
    
    @SuppressWarnings("unchecked")
    public void deleteAll(String entityName) {
		List<Object> registros = em.createQuery("select o from" + entityName + "o").getResultList();
        for(Object obj : registros) {
            em.remove(obj);
        }
    }

}