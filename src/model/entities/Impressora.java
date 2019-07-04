package model.entities;

import java.io.Serializable;

public class Impressora implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String modelo;
	private String setor;
	private String apelido;
	private String nome;
	
	public Impressora() {
	}
	
	public Impressora(Integer id, String modelo, String setor, String apelido) {
		this.id = id;
		this.modelo = modelo;
		this.setor = setor;
		this.apelido = apelido;
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

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}
	
	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return getNome();
	}
}
