package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ImpressorasDao;
import model.entities.Impressora;

public class ImpressorasService {

	private ImpressorasDao dao = DaoFactory.createImpressorasDao();
	
	public List<Impressora> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Impressora obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Impressora obj) {
		dao.removeById(obj.getId());
	}
}
