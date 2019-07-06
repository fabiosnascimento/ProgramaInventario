package model.dao;

import db.DB;
import model.dao.impl.ImpressorasDaoJDBC;
import model.dao.impl.ItensDaoJDBC;

public class DaoFactory {

	public static ItensDao createItensDao() {
		return new ItensDaoJDBC(DB.getConnection());
	}
	
	public static ImpressorasDao createImpressorasDao() {
		return new ImpressorasDaoJDBC(DB.getConnection());
	}
}
