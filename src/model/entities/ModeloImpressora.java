package model.entities;

import java.io.Serializable;

public class ModeloImpressora implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idmodeloimpressora;
	private String modelo;
	
	public ModeloImpressora(Integer idmodeloimpressora, String modelo) {
		this.idmodeloimpressora = idmodeloimpressora;
		this.modelo = modelo;
	}
	
	public ModeloImpressora() {
	}

	public Integer getIdmodeloimpressora() {
		return idmodeloimpressora;
	}

	public void setIdmodeloimpressora(Integer idmodeloimpressora) {
		this.idmodeloimpressora = idmodeloimpressora;
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
		result = prime * result + ((idmodeloimpressora == null) ? 0 : idmodeloimpressora.hashCode());
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
		if (idmodeloimpressora == null) {
			if (other.idmodeloimpressora != null)
				return false;
		} else if (!idmodeloimpressora.equals(other.idmodeloimpressora))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ModeloImpressora [idmodeloimpressora=" + idmodeloimpressora + ", modelo=" + modelo + "]";
	}
}
