package ufc.npi.prontuario.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.model.enums.TipoEstrutura;
import ufc.npi.prontuario.view.ProntuarioView;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Estrutura {

	@Id
	@SequenceGenerator(name = "estrutura_id_seq", sequenceName = "estrutura_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estrutura_id_seq")
	@JsonView(ProntuarioView.OdontogramaView.class)
	private Integer id;

	@ManyToOne
	@JsonIgnore
	private Paciente paciente;

	@OneToMany(mappedBy = "estrutura")
	private List<Patologia> patologias;

	@OneToMany(mappedBy = "estrutura")
	private List<Procedimento> procedimentos;
	
	@Enumerated(EnumType.STRING)
	@JsonView({ProntuarioView.PeriogramaView.class, ProntuarioView.OdontogramaView.class})
	private TipoEstrutura tipoEstrutura;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	
	

	public List<Patologia> getPatologias() {
		return patologias;
	}

	public void setPatologias(List<Patologia> patologias) {
		this.patologias = patologias;
	}

	public List<Procedimento> getProcedimentos() {
		return procedimentos;
	}

	public void setProcedimentos(List<Procedimento> procedimentos) {
		this.procedimentos = procedimentos;
	}

	public TipoEstrutura getTipoEstrutura() {
		return tipoEstrutura;
	}

	public void setTipoEstrutura(TipoEstrutura tipo) {
		this.tipoEstrutura = tipo;
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
		Estrutura other = (Estrutura) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
