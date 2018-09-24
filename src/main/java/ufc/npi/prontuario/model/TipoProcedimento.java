package ufc.npi.prontuario.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.view.ProntuarioView;

@Entity
public class TipoProcedimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@JsonView(ProntuarioView.OdontogramaView.class)
	private String nome;

	@NotNull
	private String descricao;

	@NotNull
	@Column(columnDefinition = "VARCHAR(255) default ''")
	private String codIdentificador;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		TipoProcedimento other = (TipoProcedimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getCodIdentificador() {
		return codIdentificador;
	}

	public void setCodIdentificador(String codIdentificador) {
		this.codIdentificador = codIdentificador;
	}

}
