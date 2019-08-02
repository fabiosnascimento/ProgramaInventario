package model.services;

import java.util.List;

import model.dao.CadSetorDao;
import model.dao.DaoFactory;
import model.entities.Setor;

public class CadSetorService {
	
	private CadSetorDao dao = DaoFactory.createCadSetorDao();
	
	public List<Setor> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Setor obj) {
		if (obj.getIdSetor() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Setor obj) {
		dao.removeById(obj.getIdSetor());
	}
}
