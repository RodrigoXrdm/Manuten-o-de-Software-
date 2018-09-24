package ufc.npi.prontuario.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Documento {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Transient
	private byte[] arquivo;
	
	@NotNull
	private String nome;
	
	@NotNull
	private String caminho;
	
	@ManyToOne
	private Paciente paciente;
	
	@Enumerated(EnumType.STRING)
	private TipoDocumento tipo;

	public Documento() {
		super();
	}

	public Documento(byte[] arquivo, String nome, String caminho) {
		super();
		this.arquivo = arquivo;
		this.nome = nome;
		this.caminho = caminho;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	
	public TipoDocumento getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumento tipo) {
		this.tipo = tipo;
	}


	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}




	public enum TipoDocumento {
		PDF("pdf"), JPG("jpg"), JPEG("jpeg"), PNG("png"), GIF("gif");
		
		private String descricao;
		
		private TipoDocumento(String descricao){
			this.descricao = descricao;
		}
		
		public String getDescricao() {
			return descricao;
		}
	}


	public String toStringRegistro() {
		return "Documento: " + "\n"+ 
				"Id: " + id + "\n" +
				"Nome: " + nome + "\n" +
				"Caminho: " + caminho + "\n" +
				"Id do Paciente: " + paciente.getId() + "\n" +
				"Tipo: " + tipo.descricao;
	}
	
	
}
