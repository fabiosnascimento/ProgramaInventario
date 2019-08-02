package model.dao;

import java.util.List;

import model.entities.Setor;

public interface CadSetorDao {

	void insert(Setor obj);
	void update(Setor obj);
	void removeById(Integer id);
	Setor findById(Integer id);
	List<Setor> findAll();
}
