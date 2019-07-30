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
import model.dao.CadModeloImpressoraDao;
import model.entities.ModeloImpressora;

public class CadModeloImpressoraDaoJDBC implements CadModeloImpressoraDao {
	
	private Connection con;
	
	public CadModeloImpressoraDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(ModeloImpressora obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO tbl_modeloimpressora (idfabricanteimpressora, modelo) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, obj.getIdFabricanteImpressora());
			st.setString(2, obj.getModelo());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdModeloImpressora(id);
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
	public void update(ModeloImpressora obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE tbl_modeloimpressora SET modelo = ? WHERE idmodeloimpressora = ?");
			st.setString(1, obj.getModelo());
			st.setInt(2, obj.getIdModeloImpressora());
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
			st = con.prepareStatement("DELETE FROM tbl_modeloimpressora WHERE idmodeloimpressora = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}
	
	@Override
	public List<ModeloImpressora> findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT tbl_fabricanteimpressora.fabricante, tbl_modeloimpressora.idModeloImpressora, tbl_modeloimpressora.modelo " + 
										"FROM tbl_fabricanteimpressora " + 
										"INNER JOIN tbl_modeloimpressora " + 
										"ON tbl_fabricanteimpressora.idfabricanteimpressora = tbl_modeloimpressora.idfabricanteimpressora " + 
										"WHERE tbl_fabricanteimpressora.idfabricanteimpressora = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			List<ModeloImpressora> list = new ArrayList<>();
			
			while (rs.next()) {
				ModeloImpressora modelo = instantiateModeloImpressora(rs);
				list.add(modelo);
			}
			return list;
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<ModeloImpressora> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT tbl_fabricanteimpressora.fabricante, tbl_modeloimpressora.idmodeloimpressora, tbl_modeloimpressora.modelo " + 
										"FROM tbl_fabricanteimpressora " + 
										"INNER JOIN tbl_modeloimpressora " + 
										"ON tbl_fabricanteimpressora.idfabricanteimpressora = tbl_modeloimpressora.idfabricanteimpressora");
			rs = st.executeQuery();
			List<ModeloImpressora> list = new ArrayList<>();
			
			while (rs.next()) {
				ModeloImpressora modelo = instantiateModeloImpressora(rs);
				list.add(modelo);
			}
			return list;
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	private ModeloImpressora instantiateModeloImpressora(ResultSet rs) throws SQLException {
		ModeloImpressora modelo = new ModeloImpressora();
		modelo.setFabricante(rs.getString("fabricante"));
		modelo.setIdModeloImpressora(rs.getInt("idmodeloimpressora"));
		modelo.setModelo(rs.getString("modelo"));
		return modelo;
	}
}
