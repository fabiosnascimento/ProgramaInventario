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
import model.dao.MaterialDao;
import model.entities.Material;

public class MaterialDaoJDBC implements MaterialDao {

	private Connection con;

	public MaterialDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Material obj) {
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
	public void update(Material obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE itens SET nome = ?, quantidade = ?, marca = ? WHERE id = ?");
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getQuantidade());
			st.setString(3, obj.getMarca());
			st.setInt(4, obj.getId());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void removeById(Integer id) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM itens WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Material findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT itens.* FROM itens WHERE id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Material material = instantiateMaterial(rs);
				return material;
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
	public List<Material> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM itens ORDER BY nome");
			rs = st.executeQuery();
			List<Material> list = new ArrayList<>();

			while (rs.next()) {
				Material material = instantiateMaterial(rs);
				list.add(material);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	private Material instantiateMaterial(ResultSet rs) throws SQLException {
		Material material = new Material();
		material.setId(rs.getInt("id"));
		material.setNome(rs.getString("nome"));
		material.setQuantidade(rs.getInt("quantidade"));
		material.setMarca(rs.getString("marca"));
		return material;
	}
}
