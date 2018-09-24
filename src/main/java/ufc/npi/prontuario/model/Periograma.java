package ufc.npi.prontuario.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import ufc.npi.prontuario.model.enums.StatusPeriograma;

@Entity
public class Periograma extends Ficha {

	@Enumerated(EnumType.STRING)
	private StatusPeriograma status;

	public StatusPeriograma getStatus() {
		return status;
	}

	public void setStatus(StatusPeriograma status) {
		this.status = status;
	}  
	
	
	
}
