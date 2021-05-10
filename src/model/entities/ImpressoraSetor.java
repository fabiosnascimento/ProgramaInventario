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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSetor == null) ? 0 : idSetor.hashCode());
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
		if (idSetor == null) {
			if (other.idSetor != null)
				return false;
		} else if (!idSetor.equals(other.idSetor))
			return false;
		return true;
	}

	public String toString() {
		return getIdSetor().getNomeSetor();
	}
}
