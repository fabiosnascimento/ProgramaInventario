package model.dao;

import java.util.List;

import model.entities.Material;

public interface MaterialDao {

	void insert(Material obj);
	void update(Material obj);
	void removeById(Integer id);
	Material findById(Integer id);
	List<Material> findAll();
}
