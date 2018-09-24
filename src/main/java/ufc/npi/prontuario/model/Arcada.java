package ufc.npi.prontuario.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.model.enums.Aparelho;
import ufc.npi.prontuario.model.enums.TipoArcada;
import ufc.npi.prontuario.model.enums.TipoEstrutura;
import ufc.npi.prontuario.view.ProntuarioView;

@Entity
public class Arcada extends Estrutura {

	@Enumerated(EnumType.STRING)
	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private TipoArcada tipoArcada;

	@Enumerated(EnumType.STRING)
	@JsonView(ProntuarioView.PeriogramaView.class)
	private Aparelho aparelho;

	@ManyToOne
	@JsonView(ProntuarioView.OdontogramaView.class)
	private Boca boca;

	@OneToMany(mappedBy = "arcada", cascade = CascadeType.PERSIST)
	
	private List<Sextante> sextantes;
	
	public Arcada() {
		super.setTipoEstrutura(TipoEstrutura.ARCADA);
	}

	public TipoArcada getTipoArcada() {
		return tipoArcada;
	}

	public void setTipoArcada(TipoArcada tipoArcada) {
		this.tipoArcada = tipoArcada;
	}

	public Aparelho getAparelho() {
		return aparelho;
	}

	public void setAparelho(Aparelho aparelho) {
		this.aparelho = aparelho;
	}

	public Boca getBoca() {
		return boca;
	}

	public void setBoca(Boca boca) {
		this.boca = boca;
	}

	public List<Sextante> getSextantes() {
		return sextantes;
	}

	public void setSextantes(List<Sextante> sextantes) {
		this.sextantes = sextantes;
	}

}
