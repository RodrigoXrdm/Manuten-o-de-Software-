package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.view.ProntuarioView;

@Entity
public class Patologia implements Comparable<Patologia> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(ProntuarioView.PeriogramaView.class)
	private Integer id;
	
	@JsonView(ProntuarioView.PeriogramaView.class)
	private String descricao;

	@DateTimeFormat(pattern = DATE_PATTERN)
	private Date data;

	@ManyToOne(optional = false)
	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private TipoPatologia tipo;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.MERGE)
	private Tratamento tratamento;
	
	@ManyToOne
	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private Estrutura estrutura;
	
	@JsonIgnore
	@ManyToOne
	private Ficha ficha;

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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoPatologia getTipo() {
		return tipo;
	}

	public void setTipo(TipoPatologia tipo) {
		this.tipo = tipo;
	}

	public Tratamento getTratamento() {
		return tratamento;
	}

	public void setTratamento(Tratamento tratamento) {
		this.tratamento = tratamento;
	}

	public Estrutura getEstrutura() {
		return estrutura;
	}

	public void setEstrutura(Estrutura estrutura) {
		this.estrutura = estrutura;
	}

	public Ficha getFicha() {
		return ficha;
	}

	public void setFicha(Ficha ficha) {
		this.ficha = ficha;
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
		Patologia other = (Patologia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Patologia o) {
		return o.getData().compareTo(this.data);
	}

	
	public String toStringRegistro() {
		return "Alteração" + "\n" +
				"Id: " + id + "\n" +
				"Descricao: " + descricao + "\n" +
				"Data: " + data + "\n" +
				"Tipo: " + tipo + "\n" +
				"Id do Tratamento: " + (tratamento != null ? tratamento.getId() : "-");
	}

	@Override
	public String toString() {
		return "Patologia [id=" + id + ", descricao=" + descricao + ", data=" + data + ", tipo=" + tipo
				+ ", estrutura=" + estrutura + ", ficha=" + ficha + "]";
	}
	
	
	
	
}
