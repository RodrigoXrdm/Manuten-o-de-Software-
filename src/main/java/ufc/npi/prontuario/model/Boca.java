package ufc.npi.prontuario.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import ufc.npi.prontuario.model.enums.TipoEstrutura;

@Entity
public class Boca extends Estrutura {

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "boca")
	private List<Arcada> arcadas;

	public Boca() {
		super.setTipoEstrutura(TipoEstrutura.BOCA);
	}
	
	public List<Arcada> getArcadas() {
		return arcadas;
	}

	public void setArcadas(List<Arcada> arcadas) {
		this.arcadas = arcadas;
	}

}
