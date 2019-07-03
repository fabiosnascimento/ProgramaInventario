package model.entities;

import java.io.Serializable;

public class Impressora implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String setor;
	
	public Impressora() {
	}
	
	public Impressora(String nome) {
		this.nome = nome;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
}
