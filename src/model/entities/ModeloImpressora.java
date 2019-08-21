package model.entities;

import java.io.Serializable;

public class ModeloImpressora implements Serializable {

	private static final long serialVersionUID = 1L;

	private FabricanteImpressora idFabricanteImpressora;
	private Integer idModeloImpressora;
	private FabricanteImpressora fabricante;
	private String modelo;
	
	public ModeloImpressora(FabricanteImpressora idFabricanteImpressora, Integer idModeloImpressora, FabricanteImpressora fabricante, String modelo) {
		this.idFabricanteImpressora = idFabricanteImpressora;
		this.idModeloImpressora = idModeloImpressora;
		this.fabricante = fabricante;
		this.modelo = modelo;
	}

	public ModeloImpressora() {
	}
	
	public FabricanteImpressora getIdFabricanteImpressora() {
		return idFabricanteImpressora;
	}
	
	public void setIdFabricanteImpressora(FabricanteImpressora idFabricanteImpressora) {
		this.idFabricanteImpressora = idFabricanteImpressora;
	}

	public Integer getIdModeloImpressora() {
		return idModeloImpressora;
	}

	public void setIdModeloImpressora(Integer idModeloImpressora) {
		this.idModeloImpressora = idModeloImpressora;
	}

	public FabricanteImpressora getFabricante() {
		return fabricante;
	}
	
	public void setFabricante(FabricanteImpressora fabricante) {
		this.fabricante = fabricante;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModeloImpressora other = (ModeloImpressora) obj;
		if (modelo == null) {
			if (other.modelo != null)
				return false;
		} else if (!modelo.equals(other.modelo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getModelo();
	}
}
