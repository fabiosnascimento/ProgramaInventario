package model.entities;

import java.io.Serializable;

public class FabricanteImpressora implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idFabricanteImpressora;
	private String fabricante;
	
	public FabricanteImpressora(Integer idFabricanteImpressora, String fabricante) {
		this.idFabricanteImpressora = idFabricanteImpressora;
		this.fabricante = fabricante;
	}
	
	public FabricanteImpressora() {
	}

	public Integer getIdFabricanteImpressora() {
		return idFabricanteImpressora;
	}

	public void setIdFabricanteImpressora(Integer idFabricanteImpressora) {
		this.idFabricanteImpressora = idFabricanteImpressora;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fabricante == null) ? 0 : fabricante.hashCode());
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
		FabricanteImpressora other = (FabricanteImpressora) obj;
		if (fabricante == null) {
			if (other.fabricante != null)
				return false;
		} else if (!fabricante.equals(other.fabricante))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FabricanteImpressora [idFabricanteImpressora=" + idFabricanteImpressora + ", fabricante=" + fabricante
				+ "]";
	}
}
