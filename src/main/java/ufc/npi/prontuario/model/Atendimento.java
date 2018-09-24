package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ufc.npi.prontuario.model.enums.StatusAtendimento;

@Entity
public class Atendimento implements Comparable<Atendimento> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@DateTimeFormat(pattern = DATE_PATTERN)
	private Date data;

	@ManyToOne(optional = false)
	private Paciente paciente;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Aluno responsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	private Aluno ajudante;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Turma turma;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Servidor professor;

	@Enumerated(EnumType.STRING)
	private StatusAtendimento status;

	@OneToOne(cascade = { CascadeType.ALL, CascadeType.REMOVE })
	private AvaliacaoAtendimento avaliacao;

	@OneToMany(mappedBy = "atendimento")
	private List<Ficha> fichas;
	
	@OneToMany(mappedBy = "atendimento")
	private List<Tratamento> tratamentos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@JsonIgnore
	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	@JsonIgnore
	public Aluno getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Aluno responsavel) {
		this.responsavel = responsavel;
	}

	@JsonIgnore
	public Aluno getAjudante() {
		return ajudante;
	}

	public void setAjudante(Aluno ajudante) {
		this.ajudante = ajudante;
	}

	@JsonIgnore
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	@JsonIgnore
	public Servidor getProfessor() {
		return professor;
	}

	public void setProfessor(Servidor professor) {
		this.professor = professor;
	}

	public StatusAtendimento getStatus() {
		return status;
	}

	public void setStatus(StatusAtendimento status) {
		this.status = status;
	}

	public AvaliacaoAtendimento getAvaliacao() {
		return this.avaliacao;
	}

	public void setAvaliacao(AvaliacaoAtendimento avaliacao) {
		this.avaliacao = avaliacao;
	}

	public List<Ficha> getFichas() {
		return fichas;
	}

	public void setFichas(List<Ficha> fichas) {
		this.fichas = fichas;
	}

	public List<Tratamento> getTratamentos() {
		return tratamentos;
	}

	public void setTratamentos(List<Tratamento> tratamentos) {
		this.tratamentos = tratamentos;
	}

	public boolean isValidado() {
		return status.equals(StatusAtendimento.VALIDADO);
	}

	public boolean isVisivel(String email) {
		return status.equals(StatusAtendimento.VALIDADO) || responsavel.getEmail().equals(email)
				|| (ajudante != null && ajudante.getEmail().equals(email)) || professor.getEmail().equals(email);
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
		Atendimento other = (Atendimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Atendimento o) {
		return o.getData().compareTo(this.data);
	}

	public String toStringRegistro() {

		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String dataFormatada = fmt.format(data);

		return "Atendimento:" + "\n" + "Id: " + id + "\n" + "Data: " + dataFormatada + "\n" + "Responsável: "
				+ responsavel.getNome() + "\n" + "Id do Responsavel: " + responsavel.getId() + " \n" + "Ajudante :"
				+ (ajudante != null ? ajudante.getNome() : "-") + "\n" + "Id do Ajudante: " + (ajudante != null ? ajudante.getId() : "-") + "\n" + "Turma: " + turma.getNome()
				+ "\n" + "Id do Turma: " + turma.getId() + "\n" + "Professor: " + professor.getNome() + "\n"
				+ "Id do Professor: " + professor.getId();
	}

}
