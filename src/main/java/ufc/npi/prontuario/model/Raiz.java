package ufc.npi.prontuario.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.model.enums.NomeRaiz;
import ufc.npi.prontuario.model.enums.TipoEstrutura;
import ufc.npi.prontuario.view.ProntuarioView;

@Entity
public class Raiz extends Estrutura {

	private Boolean presente;

	@Enumerated(EnumType.STRING)
	private NomeRaiz nome;

	@ManyToOne
	@JsonIgnore
	private Dente dente;

	@ManyToMany
	@JoinTable(name = "raiz_furca", joinColumns = { @JoinColumn(name = "raiz_id") }, inverseJoinColumns = {
	@JoinColumn(name = "furca_id") })
	@JsonView(ProntuarioView.OdontogramaView.class)
	private List<Furca> furcas;

	
	public Raiz() {
		super();
		this.furcas = new ArrayList<Furca>();
		super.setTipoEstrutura(TipoEstrutura.RAIZ);
	}

	public Boolean getPresente() {
		return presente;
	}

	public void setPresente(Boolean presente) {
		this.presente = presente;
	}

	public NomeRaiz getNome() {
		return nome;
	}

	public void setNome(NomeRaiz nome) {
		this.nome = nome;
	}

	public Dente getDente() {
		return dente;
	}

	public void setDente(Dente dente) {
		this.dente = dente;
	}

	public boolean addFurca(Furca furca){
		return this.furcas.add(furca);
	}
	
	public List<Furca> getFurcas() {
		return furcas;
	}

	public void setFurcas(List<Furca> furcas) {
		this.furcas = furcas;
	}
}
