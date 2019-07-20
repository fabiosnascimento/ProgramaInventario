package model.services;

import java.util.List;

import model.dao.CadFabricanteImpressoraDao;
import model.dao.DaoFactory;
import model.entities.FabricanteImpressora;

public class CadFabricanteImpressoraService {

	private CadFabricanteImpressoraDao dao = DaoFactory.createCadFabricanteImpressoraDao();
	
	public List<FabricanteImpressora> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(FabricanteImpressora obj) {
		if (obj.getIdFabricanteImpressora() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(FabricanteImpressora obj) {
		dao.removeById(obj.getIdFabricanteImpressora());
	}
}
