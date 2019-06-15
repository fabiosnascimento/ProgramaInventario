package application;

import model.dao.DaoFactory;
import model.dao.ItensDao;
import model.entities.Itens;

public class Main {

	public static void main(String[] args) {

		ItensDao itensDao = DaoFactory.createItensDao();
		
		Itens item = itensDao.findById(5);
		item.setQuantidade(2);
		itensDao.update(item);
	}
		
}
