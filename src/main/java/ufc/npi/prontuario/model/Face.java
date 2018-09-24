package ufc.npi.prontuario.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.TipoEstrutura;
import ufc.npi.prontuario.view.ProntuarioView;

@Entity
public class Face extends Estrutura {

	@Enumerated(EnumType.STRING)
	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private NomeFace nome;

	@ManyToOne
	@JsonView(ProntuarioView.PeriogramaView.class)
	private Dente dente;
	
	public Face() {
		super.setTipoEstrutura(TipoEstrutura.FACE);
	}

	public NomeFace getNome() {
		return nome;
	}

	public void setNome(NomeFace nome) {
		this.nome = nome;
	}

	public Dente getDente() {
		return dente;
	}

	public void setDente(Dente dente) {
		this.dente = dente;
	}
}
