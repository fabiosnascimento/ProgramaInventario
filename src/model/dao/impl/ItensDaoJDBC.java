package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.ItensDao;
import model.entities.Itens;

public class ItensDaoJDBC implements ItensDao {

	private Connection con;
	
	public ItensDaoJDBC(Connection con) {
		this.con = con;
	}
	
	@Override
	public void insert(Itens obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Itens obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Itens findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT itens.* FROM itens WHERE id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Itens item = new Itens();
				item.setId(rs.getInt("id"));
				item.setNome(rs.getString("nome"));
				item.setQuantidade(rs.getInt("quantidade"));
				item.setMarca(rs.getString("marca"));
				return item;
			}
			return null;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Itens> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
