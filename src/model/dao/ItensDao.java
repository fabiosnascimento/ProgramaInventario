package model.dao;

import java.util.List;

import model.entities.Itens;

public interface ItensDao {

	void inserir(Itens obj);
	void atualizar(Itens obj);
	void removerPorId(Integer id);
	Itens buscaPorId(Integer id);
	List<Itens> findAll();
}
