package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.MaterialDao;
import model.entities.Material;

public class MaterialService {
	
	private MaterialDao dao = DaoFactory.createMaterialDao();
	
	public List<Material> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Material obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Material obj) {
		dao.removeById(obj.getId());
	}
}
