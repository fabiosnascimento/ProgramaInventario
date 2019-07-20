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
import model.dao.CadFabricanteImpressoraDao;
import model.entities.FabricanteImpressora;

public class CadFabricanteImpressoraDaoJDBC implements CadFabricanteImpressoraDao {

	private Connection con;
	
	public CadFabricanteImpressoraDaoJDBC(Connection con) {
		this.con = con;
	}
	
	@Override
	public void insert(FabricanteImpressora obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO tbl_fabricanteimpressora (fabricante) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getFabricante());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdFabricanteImpressora(id);
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
	public void update(FabricanteImpressora obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE tbl_fabricanteimpressora SET fabricante = ? WHERE idfabricanteimpressora = ?");
			st.setString(1, obj.getFabricante());
			st.setInt(2, obj.getIdFabricanteImpressora());
			st.executeUpdate();
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void removeById(Integer id) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM tbl_fabricanteimpressora WHERE idfabricanteimpressora = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public FabricanteImpressora findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT tbl_fabricanteimpressora.* FROM tbl_fabricanteimpressora WHERE idfabricanteimpressora = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if (rs.next()) {
				FabricanteImpressora obj = instantiateFabImpressora(rs);
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
	public List<FabricanteImpressora> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM tbl_fabricanteimpressora");
			
			rs = st.executeQuery();
			List<FabricanteImpressora> list = new ArrayList<>();
			
			while (rs.next()) {
				FabricanteImpressora obj = instantiateFabImpressora(rs);
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
	
	private FabricanteImpressora instantiateFabImpressora(ResultSet rs) throws SQLException {
		FabricanteImpressora obj = new FabricanteImpressora();
		obj.setIdFabricanteImpressora(rs.getInt("idfabricanteimpressora"));
		obj.setFabricante(rs.getString("fabricante"));
		return obj;
	}

}
