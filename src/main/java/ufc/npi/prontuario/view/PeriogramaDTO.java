package ufc.npi.prontuario.view;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.model.Arcada;
import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Patologia;

public class PeriogramaDTO {
	@JsonView(ProntuarioView.PeriogramaView.class)
	private List<Patologia> patologias;

	@JsonView(ProntuarioView.PeriogramaView.class)
	private List<Arcada> arcadas;

	@JsonView(ProntuarioView.PeriogramaView.class)
	private List<Dente> dentes;

	public List<Patologia> getPatologias() {
		return patologias;
	}

	public void setPatologias(List<Patologia> patologias) {
		this.patologias = patologias;
	}

	public List<Arcada> getArcadas() {
		return arcadas;
	}

	public void setArcadas(List<Arcada> arcadas) {
		this.arcadas = arcadas;
	}

	public List<Dente> getDentes() {
		return dentes;
	}

	public void setDentes(List<Dente> dentes) {
		this.dentes = dentes;
	}
}
