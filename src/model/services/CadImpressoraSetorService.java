package model.services;

import java.util.List;

import model.dao.CadImpressoraSetorDao;
import model.dao.DaoFactory;
import model.entities.ImpressoraSetor;

public class CadImpressoraSetorService {

	private CadImpressoraSetorDao dao = DaoFactory.createCadImpressoraSetorDao();
	
	public List<ImpressoraSetor> findAll() {
		return dao.findAll();
	}
	
	public void save(ImpressoraSetor obj) {
		dao.insert(obj);
	}
	
//	public void saveOrUpdate(ImpressoraSetor obj) {
//		if (obj.getIdModeloImpressora() == null) {
//			dao.insert(obj);
//		} else {
//			dao.update(obj);
//		}
//	}
	
	public void remove(ImpressoraSetor obj) {
		dao.removeById(obj.getIdImpressoraSetor());
	}
	
	public List<ImpressoraSetor> findById(Integer id) {
		return dao.findById(id);
	}
}
