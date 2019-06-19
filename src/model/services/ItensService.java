package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ItensDao;
import model.entities.Itens;

public class ItensService {
	
	private ItensDao dao = DaoFactory.createItensDao();
	
	public List<Itens> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Itens obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

}
