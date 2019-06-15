package application;

import model.dao.DaoFactory;
import model.dao.ItensDao;

public class Main {

	public static void main(String[] args) {

		ItensDao itensDao = DaoFactory.createItensDao();
		
		itensDao.removeById(5);
	}
		
}
