package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.view.ProntuarioView;

@Entity
public class Procedimento implements Comparable<Procedimento> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(optional = false)
	@JsonView(ProntuarioView.OdontogramaView.class)
	private TipoProcedimento tipoProcedimento;

	@JsonView(ProntuarioView.OdontogramaView.class)
	private String descricao;

	@JsonView(ProntuarioView.OdontogramaView.class)
	private Boolean preExistente;

	@DateTimeFormat(pattern = DATE_PATTERN)
	@JsonView(ProntuarioView.OdontogramaView.class)
	private Date data;
	
	@ManyToOne
	@JsonIgnore
	@JsonView(ProntuarioView.OdontogramaView.class)
	private Ficha ficha;
	
	@ManyToOne
	@JsonView(ProntuarioView.OdontogramaView.class)
	private Estrutura estrutura;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoProcedimento getTipoProcedimento() {
		return tipoProcedimento;
	}

	public void setTipoProcedimento(TipoProcedimento tipoProcedimento) {
		this.tipoProcedimento = tipoProcedimento;
	}

	public Boolean getPreExistente() {
		return preExistente;
	}

	public void setPreExistente(Boolean preExistente) {
		this.preExistente = preExistente;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Ficha getFicha() {
		return ficha;
	}

	public void setFicha(Ficha ficha) {
		this.ficha = ficha;
	}

	public Estrutura getEstrutura() {
		return estrutura;
	}

	public void setEstrutura(Estrutura estrutura) {
		this.estrutura = estrutura;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Procedimento other = (Procedimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Procedimento p) {
		return p.getData().compareTo(this.data);
	}

	public String toStringRegistro() {
		return "Procedimento:" + "\n" +
				"Id: " + id + "\n" +
				"Tipo de Procedimento: " + tipoProcedimento.getDescricao() + "\n" +
				"Descricao: " + descricao + "\n" +
				"Pre Existente: " + (preExistente ? "sim" : "não");
	}
	
}
