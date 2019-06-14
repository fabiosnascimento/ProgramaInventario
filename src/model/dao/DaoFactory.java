package model.dao;

import db.DB;
import model.dao.impl.ItensDaoJDBC;

public class DaoFactory {

	public static ItensDao createItensDao() {
		return new ItensDaoJDBC(DB.getConnection());
	}
}
