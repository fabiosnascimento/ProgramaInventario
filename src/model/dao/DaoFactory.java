package model.dao;

import db.DB;
import model.dao.impl.CadFabricanteImpressoraDaoJDBC;
import model.dao.impl.CadImpressoraSetorDaoJDBC;
import model.dao.impl.CadModeloImpressoraDaoJDBC;
import model.dao.impl.CadSetorDaoJDBC;
import model.dao.impl.MaterialDaoJDBC;

public class DaoFactory {

	public static MaterialDao createMaterialDao() {
		return new MaterialDaoJDBC(DB.getConnection());
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
	
	public static CadImpressoraSetorDao createCadImpressoraSetorDao() {
		return new CadImpressoraSetorDaoJDBC(DB.getConnection());
	}
}
