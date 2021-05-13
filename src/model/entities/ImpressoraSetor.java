package model.entities;

import java.io.Serializable;

public class ImpressoraSetor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idImpressoraSetor;
	private Setor idSetor;
	private FabricanteImpressora idFabricante;
	private ModeloImpressora idModelo;
	
	public ImpressoraSetor(Integer idImpressoraSetor, Setor idSetor, FabricanteImpressora idFabricante, ModeloImpressora idModelo) {
		this.idImpressoraSetor = idImpressoraSetor;
		
		this.idSetor = idSetor;
		this.idFabricante = idFabricante;
		this.idModelo = idModelo;
	}
	
	public ImpressoraSetor() {
	}

	public Integer getIdImpressoraSetor() {
		return idImpressoraSetor;
	}

	public void setIdImpressoraSetor(Integer idImpressoraSetor) {
		this.idImpressoraSetor = idImpressoraSetor;
	}

	public Setor getIdSetor() {
		return idSetor;
	}

	public void setIdSetor(Setor idSetor) {
		this.idSetor = idSetor;
	}

	public FabricanteImpressora getIdFabricanteImpressora() {
		return idFabricante;
	}

	public void setIdFabricanteImpressora(FabricanteImpressora idFabricanteImpressora) {
		this.idFabricante = idFabricanteImpressora;
	}

	public ModeloImpressora getIdModeloImpressora() {
		return idModelo;
	}

	public void setIdModeloImpressora(ModeloImpressora idModeloImpressora) {
		this.idModelo = idModeloImpressora;
	}

	public String toString() {
		return getIdSetor().getNomeSetor();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idFabricante == null) ? 0 : idFabricante.hashCode());
		result = prime * result + ((idModelo == null) ? 0 : idModelo.hashCode());
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
		ImpressoraSetor other = (ImpressoraSetor) obj;
		if (idFabricante == null) {
			if (other.idFabricante != null)
				return false;
		} else if (!idFabricante.equals(other.idFabricante))
			return false;
		if (idModelo == null) {
			if (other.idModelo != null)
				return false;
		} else if (!idModelo.equals(other.idModelo))
			return false;
		return true;
	}
}
