package br.com.javamagazine.jee6.crud.cliente.entity.filter;

import java.io.Serializable;

public class ClienteFilter implements Serializable {
	
	private static final long serialVersionUID = 7583323048545278763L;
	
	private final String nome;
	
	public ClienteFilter(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
}