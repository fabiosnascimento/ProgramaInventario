package model.dao;

import java.util.List;

import model.entities.ImpressoraSetor;

public interface CadImpressoraSetorDao {

	void insert(ImpressoraSetor obj);
	void update(ImpressoraSetor obj);
	void removeById(Integer id);
	List<ImpressoraSetor> findById(Integer id);
	List<ImpressoraSetor> findAll();
}
