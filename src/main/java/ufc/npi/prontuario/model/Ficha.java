package ufc.npi.prontuario.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Ficha {

	@Id
	@SequenceGenerator(name = "ficha_id_seq", sequenceName = "ficha_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ficha_id_seq")
	private Integer id;

	@ManyToOne
	@JsonIgnore
	private Atendimento atendimento;

	@OneToMany(mappedBy = "ficha")
	private List<Patologia> patologias;

	@OneToMany(mappedBy = "ficha")
	private List<Procedimento> procedimentos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
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
	
	
}
