package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Itens;

public class ItensService {
	
	public List<Itens> findAll() {
		List<Itens> list = new ArrayList<>();
		list.add(new Itens(1, "Teclado USB novo", 3, null));
		list.add(new Itens(2, "Teclado USB usado", 5, null));
		list.add(new Itens(3, "Mouse USB novo", 4, null));
		return list;
	}

}
