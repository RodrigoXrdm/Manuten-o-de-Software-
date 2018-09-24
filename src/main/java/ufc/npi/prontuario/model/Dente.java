package ufc.npi.prontuario.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.model.enums.NumeroDente;
import ufc.npi.prontuario.model.enums.TipoDente;
import ufc.npi.prontuario.model.enums.TipoEstrutura;
import ufc.npi.prontuario.view.ProntuarioView;

@Entity
public class Dente extends Estrutura {

	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private Boolean presente;

	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private Boolean implante;

	@ManyToOne
	@JsonView(ProntuarioView.OdontogramaView.class)
	private Sextante sextante;

	@Enumerated(EnumType.STRING)
	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private TipoDente tipo;

	@Enumerated(EnumType.ORDINAL)
	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private NumeroDente numero;

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "dente")
	@JsonView(ProntuarioView.OdontogramaView.class)
	private List<Face> faces;

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "dente")
	@JsonView(ProntuarioView.OdontogramaView.class)
	private List<Raiz> raizes;
	
	public Dente() {
		super.setTipoEstrutura(TipoEstrutura.DENTE);
	}

	public Boolean getPresente() {
		return presente;
	}

	public void setPresente(Boolean presente) {
		this.presente = presente;
	}

	public Boolean getImplante() {
		return implante;
	}

	public void setImplante(Boolean implante) {
		this.implante = implante;
	}

	public Sextante getSextante() {
		return sextante;
	}

	public void setSextante(Sextante sextante) {
		this.sextante = sextante;
	}

	public TipoDente getTipo() {
		return tipo;
	}

	public void setTipo(TipoDente tipo) {
		this.tipo = tipo;
	}

	public NumeroDente getNumero() {
		return numero;
	}

	public void setNumero(NumeroDente numero) {
		this.numero = numero;
	}

	public List<Face> getFaces() {
		return faces;
	}

	public void setFaces(List<Face> faces) {
		this.faces = faces;
	}

	public List<Raiz> getRaizes() {
		return raizes;
	}

	public void setRaizes(List<Raiz> raizes) {
		this.raizes = raizes;
	}
	
	public Dente(Boolean presente, Boolean implante, Sextante sextante, TipoDente tipo, NumeroDente numero) {
		super();
		this.presente = presente;
		this.implante = implante;
		this.sextante = sextante;
		this.tipo = tipo;
		this.numero = numero;
	}
	
	
}
