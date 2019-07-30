package model.dao;

import java.util.List;

import model.entities.ModeloImpressora;

public interface CadModeloImpressoraDao {

	void insert(ModeloImpressora obj);
	void update(ModeloImpressora obj);
	void removeById(Integer id);
	List<ModeloImpressora> findById(Integer id);
	List<ModeloImpressora> findAll();
}
