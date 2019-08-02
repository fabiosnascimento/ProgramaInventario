package model.entities;

import java.io.Serializable;

public class Setor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idSetor;
	private String nomeSetor;
	
	public Setor(Integer idSetor, String nomeSetor) {
		this.idSetor = idSetor;
		this.nomeSetor = nomeSetor;
	}
	
	public Setor() {
	}
	
	public Integer getIdSetor() {
		return idSetor;
	}
	
	public void setIdSetor(Integer idSetor) {
		this.idSetor = idSetor;
	}
	
	public String getNomeSetor() {
		return nomeSetor;
	}
	
	public void setNomeSetor(String nomeSetor) {
		this.nomeSetor = nomeSetor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeSetor == null) ? 0 : nomeSetor.hashCode());
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
		Setor other = (Setor) obj;
		if (nomeSetor == null) {
			if (other.nomeSetor != null)
				return false;
		} else if (!nomeSetor.equals(other.nomeSetor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nomeSetor;
	}
}
