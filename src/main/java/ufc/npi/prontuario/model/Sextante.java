package ufc.npi.prontuario.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.model.enums.NomeSextante;
import ufc.npi.prontuario.model.enums.TipoEstrutura;
import ufc.npi.prontuario.view.ProntuarioView;

@Entity
public class Sextante extends Estrutura {

	@Enumerated(EnumType.STRING)
	@JsonView(ProntuarioView.PeriogramaView.class)
	private NomeSextante nome;

	@OneToMany(mappedBy = "sextante", cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<Dente> dentes;
	
	@ManyToOne
	@JsonView(ProntuarioView.OdontogramaView.class)
	private Arcada arcada;
	
	public Sextante() {
		super.setTipoEstrutura(TipoEstrutura.SEXTANTE);
	}

	public NomeSextante getNome() {
		return nome;
	}

	public void setNome(NomeSextante nome) {
		this.nome = nome;
	}

	public List<Dente> getDentes() {
		return dentes;
	}

	public void setDentes(List<Dente> dentes) {
		this.dentes = dentes;
	}

	public Arcada getArcada() {
		return arcada;
	}

	public void setArcada(Arcada arcada) {
		this.arcada = arcada;
	}
	
}
