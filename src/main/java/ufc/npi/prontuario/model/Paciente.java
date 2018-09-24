package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import ufc.npi.prontuario.model.enums.Estado;
import ufc.npi.prontuario.model.enums.EstadoCivil;
import ufc.npi.prontuario.model.enums.Raca;
import ufc.npi.prontuario.model.enums.Sexo;

@Entity
public class Paciente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	private String nome;
	
	@NotNull
	@Column(columnDefinition = "VARCHAR(255) DEFAULT 'Não informado'")
	private String nomeDaMae;

	@NotNull
	@DateTimeFormat(pattern = DATE_PATTERN)
	private Date nascimento;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	private Raca raca;
	
	private String nacionalidade;
	
	private String naturalidade;

	private String cpf;
	
	private String cns;

	private String profissao;

	@Enumerated(EnumType.STRING)
	private EstadoCivil estadoCivil;

	private String responsavel;

	@NotNull
	@Column(columnDefinition = "VARCHAR(255) DEFAULT 'Não informado'")
	private String telefone;
	
	private String segundoTelefone;

	private String endereco;

	private String bairro;

	private String cidade;
	
	private String cep;
	
	private String pais;

	@Enumerated(EnumType.STRING)
	private Estado estado;

	@OrderBy("data DESC")
	@OneToMany(mappedBy = "paciente", cascade = CascadeType.MERGE)
	private List<PacienteAnamnese> pacienteAnamneses;

	@OrderBy("data DESC")
	@OneToMany(mappedBy = "paciente", cascade = CascadeType.MERGE)
	private List<Atendimento> atendimentos;
	
	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "paciente")
	private List<Documento> documentos;
	
	@OneToMany(mappedBy = "paciente")
	private List<PlanoTratamento> tratamentos;
	
	@OneToOne(mappedBy = "paciente")
	private Boca boca;

	public String getNomeDaMae() {
		return nomeDaMae;
	}

	public void setNomeDaMae(String nomeDaMae) {
		this.nomeDaMae = nomeDaMae;
	}

	public Raca getRaca() {
		return raca;
	}

	public void setRaca(Raca raca) {
		this.raca = raca;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getCns() {
		return cns;
	}

	public void setCns(String numeroCNS) {
		this.cns = numeroCNS;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

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

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getSegundoTelefone() {
		return segundoTelefone;
	}

	public void setSegundoTelefone(String segundoTelefone) {
		this.segundoTelefone = segundoTelefone;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<PacienteAnamnese> getPacienteAnamneses() {
		return pacienteAnamneses;
	}

	public void setPacienteAnamneses(List<PacienteAnamnese> pacienteAnamneses) {
		this.pacienteAnamneses = pacienteAnamneses;
	}

	public void addPacienteAnamnese(PacienteAnamnese anamnese) {
		if (this.pacienteAnamneses == null) {
			this.pacienteAnamneses = new ArrayList<>();
		}

		this.pacienteAnamneses.add(anamnese);
	}

	public boolean temSexo() {
        return (this.sexo != null) ? true : false;
    }
    
    public boolean temEstadoCivil() {
        return (this.estadoCivil != null) ? true : false;
    }
    
    public boolean temEstado() {
        return (this.estado != null) ? true : false;
    }

	public List<Atendimento> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<Atendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}

	public List<Documento> getDocumentos() {
		return documentos;
	}

	public void addDocumento(Documento documento) {
		if(this.documentos == null) {
			this.documentos = new ArrayList<>();
		}
		
		this.documentos.add(documento);
	}
	
	public List<PlanoTratamento> getTratamentos() {
		return tratamentos;
	}
	
	public Boca getBoca() {
		return boca;
	}
	
	public void setBoca(Boca boca) {
		this.boca = boca;
	}

	public void removerDocumento(Documento documento) {
		this.documentos.remove(documento);
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
		Paciente other = (Paciente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String toStringRegistro() {
		return "Paciente: " + "\n" + 
				"Id: "+ id + "\n" +
				"Nome: " + nome + "\n" +
				"Nascimento: " + nascimento + "\n" +
				"Sexo: " + sexo + "\n" +
				"Naturalidade: " + naturalidade + "\n" + 
				"CPF:" + cpf + "\n" +
				"Profissao: " + profissao + "\n" +
				"Estado Civil: " + estadoCivil + "\n" +
				"Responsavel: " + responsavel + "\n" +
				"Telefone: " + telefone + "\n" +
				"Segundo Telefone: " +  segundoTelefone + "\n" + 
				"Endereco: " + endereco + "\n" +
				"Bairro: " + bairro + "\n" +
				"Cidade: " + cidade + "\n" +
				"Estado: " + estado;
	}

}
