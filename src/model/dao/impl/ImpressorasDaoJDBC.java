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
import model.dao.ImpressorasDao;
import model.entities.Impressora;

public class ImpressorasDaoJDBC implements ImpressorasDao {
	
	private Connection con;
	
	public ImpressorasDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Impressora obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO impressoras (modelo, setor, apelido) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getModelo());
			st.setString(2, obj.getSetor());
			st.setString(3, obj.getApelido());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Impressora obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE impressoras SET modelo = ?, setor = ?, apelido = ? WHERE id = ?");
			st.setString(1, obj.getModelo());
			st.setString(2, obj.getSetor());
			st.setString(3, obj.getApelido());
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
			st = con.prepareStatement("DELETE FROM impressoras WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Impressora findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT impressoras.* FROM impressoras WHERE id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if (rs.next()) {
				Impressora impressora = instantiateImpressora(rs);
				return impressora;
			}
			return null;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Impressora> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM impressoras");
			
			rs = st.executeQuery();
			List<Impressora> list = new ArrayList<>();
			
			while (rs.next()) {
				Impressora impressora = instantiateImpressora(rs);
				list.add(impressora);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	private Impressora instantiateImpressora(ResultSet rs) throws SQLException {
		Impressora impressora = new Impressora();
		impressora.setId(rs.getInt("id"));
		impressora.setModelo(rs.getString("modelo"));
		impressora.setSetor(rs.getString("setor"));
		impressora.setApelido(rs.getString("apelido"));
		return impressora;
	}
}
