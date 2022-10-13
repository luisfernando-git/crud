package br.com.javamagazine.jee.crud.cliente.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ClienteTelefone implements Serializable {

	private static final long serialVersionUID = 1768448193870878261L;
	
	@NotNull
	private String telefone;
	
	@NotNull
	private String descricao;
	
	public ClienteTelefone() {
		
	}
	
	public ClienteTelefone(String telefone, String descricao) {
		this.telefone = telefone;
		this.descricao = descricao;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClienteTelefone other = (ClienteTelefone) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClienteTelefone [telefone=" + telefone + ", descricao=" + descricao + "]";
	}
}
