package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO itens (nome, quantidade, marca) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getQuantidade());
			st.setString(3, obj.getMarca());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DBException("Erro inexperado! Nenhuma linha afetada.");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
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
				Itens item = instantiateItens(rs);
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
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM itens ORDER BY nome");
			rs = st.executeQuery();
			List<Itens> list = new ArrayList<>();

			while (rs.next()) {
				Itens item = instantiateItens(rs);
				list.add(item);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	private Itens instantiateItens(ResultSet rs) throws SQLException {
		Itens item = new Itens();
		item.setId(rs.getInt("id"));
		item.setNome(rs.getString("nome"));
		item.setQuantidade(rs.getInt("quantidade"));
		item.setMarca(rs.getString("marca"));
		return item;
	}
}
