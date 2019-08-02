package model.dao;

import db.DB;
import model.dao.impl.CadFabricanteImpressoraDaoJDBC;
import model.dao.impl.CadModeloImpressoraDaoJDBC;
import model.dao.impl.CadSetorDaoJDBC;
import model.dao.impl.ItensDaoJDBC;

public class DaoFactory {

	public static ItensDao createItensDao() {
		return new ItensDaoJDBC(DB.getConnection());
	}
	
	public static CadFabricanteImpressoraDao createCadFabricanteImpressoraDao() {
		return new CadFabricanteImpressoraDaoJDBC(DB.getConnection());
	}
	
	public static CadModeloImpressoraDao createCadModeloImpressoraDao() {
		return new CadModeloImpressoraDaoJDBC(DB.getConnection());
	}
	
	public static CadSetorDao createCadSetorDao() {
		return new CadSetorDaoJDBC(DB.getConnection());
	}
}
