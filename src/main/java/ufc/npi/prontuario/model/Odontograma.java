package ufc.npi.prontuario.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Odontograma extends Ficha {

	@OneToOne(cascade = CascadeType.ALL)
	private Odontograma anterior;

	public Odontograma getAnterior() {
		return anterior;
	}

	public void setAnterior(Odontograma anterior) {
		this.anterior = anterior;
	}
}
