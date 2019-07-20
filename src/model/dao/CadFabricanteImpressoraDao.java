package model.dao;

import java.util.List;

import model.entities.FabricanteImpressora;

public interface CadFabricanteImpressoraDao {

	void insert(FabricanteImpressora obj);
	void update(FabricanteImpressora obj);
	void removeById(Integer id);
	FabricanteImpressora findById(Integer id);
	List<FabricanteImpressora> findAll();
}
