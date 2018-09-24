package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Registro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@DateTimeFormat(pattern = DATE_PATTERN)
	private Date data;
	
	private Integer idUsuario;
	
	private Integer idPaciente;
		
	@Enumerated(EnumType.ORDINAL)
	private Acao tipoAcao;
	
	private Integer valor;
	
	@Column(columnDefinition = "TEXT")
	private String observacao;
	

	public enum Acao {
		
		//log de dados do paciente		
		CADASTRAR_NOVO_PACIENTE("Cadastro de paciente"),
		EDITAR_DADOS_PACIENTE("Edição de dados do paciente"),
		
		//log de dados do atendimento
		CADASTRAR_NOVO_ATENDIMENTO("Cadastro de atendimento"),
		EDITAR_DADOS_ATENDIMENTO("Edição de dados do atendimento"),
		REMOVER_ATENDIMENTO("Remoção de atendimento"),
		VISUALIZAR_ATENDIMENTO("Visualização de atendimento"),
		FINALIZAR_ATENDIMENTO("Finalização de atendimento"),
		VALIDAR_ATENDIMENTO("Validação de atendimento"),
		AVALIAR_ATENDIMENTO("Avaliação de atendimento"),
		
		
		//log de dados do odontograma
		VISUALIZAR_ODONTOGRAMA("Visualização de odontograma"),
		CADASTAR_PROCEDIMENTO("Cadastro de procedimento"),
		REMOVER_PROCEDIMENTO("Removeção de procedimento"),
		CADASTRAR_PATOLOGIA("Cadastro de alteração realizada"),
		REMOVER_PATOLOGIA("Remoção de alteração realizada"),
		
		//Log de dados da anamnese
		CADASTRAR_ANAMNESE("Cadastro de anamnese"),
		VISUALIZAR_ANAMNESE("Visualização de anamnese"),
		EDITAR_ANAMNESE("Edição de anamnese"),
		
		//Log de dados do exame
		CADASTRAR_EXAME("Cadastro de exame"),
		VISUALIZAR_EXAME("Visualização de exame"),
		REMOVER_EXAME("Remoção de exame"),
		DOWNLOAD_EXAME("Download de exame");
		
		private String descricao;

		private Acao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public Registro(){}
	
	public Registro(Integer id, Date data, Integer idUsuario, Integer idPaciente, Acao tipoAcao) {
		super();
		this.id = id;
		this.data = data;
		this.idUsuario = idUsuario;
		this.idPaciente = idPaciente;
		this.tipoAcao = tipoAcao;
	}

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

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(Integer idPaciente) {
		this.idPaciente = idPaciente;
	}


	public Acao getTipoAcao() {
		return tipoAcao;
	}

	public void setTipoAcao(Acao tipoAcao) {
		this.tipoAcao = tipoAcao;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	
	
}
