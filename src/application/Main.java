package application;

import model.dao.DaoFactory;
import model.dao.ItensDao;
import model.entities.Itens;

public class Main {

	public static void main(String[] args) {

		ItensDao itensDao = DaoFactory.createItensDao();
		
		Itens item = new Itens(null, "Mouse USB novo", 2, null);
		itensDao.insert(item);
		System.out.println("Inserido! Novo id = " + item.getId());
	}
		
}
