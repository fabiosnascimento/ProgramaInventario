package model.services;

import java.util.List;

import model.dao.CadModeloImpressoraDao;
import model.dao.DaoFactory;
import model.entities.ModeloImpressora;

public class CadModeloImpressoraService {

	private CadModeloImpressoraDao dao = DaoFactory.createCadModeloImpressoraDao();
	
	public List<ModeloImpressora> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(ModeloImpressora obj) {
		if (obj.getIdModeloImpressora() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(ModeloImpressora obj) {
		dao.removeById(obj.getIdModeloImpressora());
	}
	
	public List<ModeloImpressora> findById(Integer id) {
		return dao.findById(id);
	}
}
