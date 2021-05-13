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
import model.dao.CadImpressoraSetorDao;
import model.entities.FabricanteImpressora;
import model.entities.ImpressoraSetor;
import model.entities.ModeloImpressora;
import model.entities.Setor;

public class CadImpressoraSetorDaoJDBC implements CadImpressoraSetorDao {
	
	private Connection con;
	
	public CadImpressoraSetorDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(ImpressoraSetor obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO tbl_impressorasetor (idsetor, idfabricante, idmodelo) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, obj.getIdSetor().getIdSetor());
			st.setInt(2, obj.getIdFabricanteImpressora().getIdFabricanteImpressora());
			st.setInt(3, obj.getIdModeloImpressora().getIdModeloImpressora());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdImpressoraSetor(id);
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
	public void update(ImpressoraSetor obj) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void removeById(Integer id) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM tbl_impressorasetor WHERE idimpressorasetor = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<ImpressoraSetor> findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT tbl_impressorasetor.idimpressorasetor, tbl_setor.setor, tbl_modeloimpressora.modelo, tbl_fabricanteimpressora.fabricante "
										+ "FROM tbl_impressorasetor " 
										+ "INNER JOIN tbl_setor ON tbl_impressorasetor.idsetor = tbl_setor.idsetor " 
										+ "INNER JOIN tbl_fabricanteimpressora ON tbl_impressorasetor.idfabricante = tbl_fabricanteimpressora.idfabricanteimpressora " 
										+ "INNER JOIN tbl_modeloimpressora ON tbl_impressorasetor.idmodelo = tbl_modeloimpressora.idmodeloimpressora "
										+ "WHERE tbl_impressorasetor.idsetor = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			List<ImpressoraSetor> list = new ArrayList<>();
			
			while(rs.next()) {
				ImpressoraSetor obj = instantiateImpressoraSetor(rs);
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

	@Override
	public List<ImpressoraSetor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT tbl_setor.setor, tbl_modeloimpressora.modelo, tbl_fabricanteimpressora.fabricante FROM tbl_impressorasetor "
										+ "INNER JOIN tbl_setor ON tbl_impressorasetor.idsetor = tbl_setor.idsetor "
										+ "INNER JOIN tbl_fabricanteimpressora ON tbl_impressorasetor.idfabricante = tbl_fabricanteimpressora.idfabricanteimpressora "
										+ "INNER JOIN tbl_modeloimpressora ON tbl_impressorasetor.idmodelo = tbl_modeloimpressora.idmodeloimpressora;");
			rs = st.executeQuery();
			List<ImpressoraSetor> list = new ArrayList<>();
			
			while (rs.next()) {
				ImpressoraSetor obj = instantiateImpressoraSetor(rs);
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

	private ImpressoraSetor instantiateImpressoraSetor(ResultSet rs) throws SQLException {
		ImpressoraSetor obj = new ImpressoraSetor();
		Setor setor = new Setor();
		FabricanteImpressora fabricante = new FabricanteImpressora();
		ModeloImpressora modelo = new ModeloImpressora();
		obj.setIdImpressoraSetor(rs.getInt("idimpressorasetor"));
		setor.setNomeSetor(rs.getString("setor"));
		fabricante.setFabricante(rs.getString("fabricante"));
		modelo.setModelo(rs.getString("modelo"));
		obj.setIdSetor(setor);
		obj.setIdFabricanteImpressora(fabricante);
		obj.setIdModeloImpressora(modelo);
		return obj;
	}
}
