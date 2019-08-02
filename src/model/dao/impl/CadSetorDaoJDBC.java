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
import model.dao.CadSetorDao;
import model.entities.Setor;

public class CadSetorDaoJDBC implements CadSetorDao {

	private Connection con;

	public CadSetorDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Setor obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO tbl_setor (setor) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNomeSetor());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdSetor(id);
				}
				DB.closeResultSet(rs);
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Setor obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE tbl_setor SET setor = ? WHERE idsetor = ?");
			st.setString(1, obj.getNomeSetor());
			st.setInt(2, obj.getIdSetor());
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
			st = con.prepareStatement("DELETE FROM tbl_setor WHERE idsetor = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Setor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT tbl_setor.* FROM tbl_setor WHERE idsetor = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if (rs.next()) {
				Setor obj = instantiateSetor(rs);
				return obj;
			}
			return null;
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Setor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM tbl_setor");
			
			rs = st.executeQuery();
			List<Setor> list = new ArrayList<>();
			
			while (rs.next()) {
				Setor obj = instantiateSetor(rs);
				list.add(obj);
			}
			return list;
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	private Setor instantiateSetor(ResultSet rs) throws SQLException {
		Setor obj = new Setor();
		obj.setIdSetor(rs.getInt("idsetor"));
		obj.setNomeSetor(rs.getString("setor"));
		return obj;
	}
		
}
