package model.dao;

import java.util.List;

import model.entities.Impressora;

public interface ImpressorasDao {
	
	void insert(Impressora obj);
	void update(Impressora obj);
	void removeById(Integer id);
	Impressora findById(Integer id);
	List<Impressora> findAll();
}
