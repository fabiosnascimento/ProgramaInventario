package model.dao;

import java.util.List;

import model.entities.Itens;

public interface ItensDao {

	void insert(Itens obj);
	void update(Itens obj);
	void removeById(Integer id);
	Itens findById(Integer id);
	List<Itens> findAll();
}
