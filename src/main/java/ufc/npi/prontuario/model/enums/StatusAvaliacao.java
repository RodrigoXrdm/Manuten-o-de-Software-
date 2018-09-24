package ufc.npi.prontuario.model.enums;

public enum StatusAvaliacao {
	EM_ANDAMENTO("Em andamento"), FINALIZADA("Finalizada");

	private String descricao;

	private StatusAvaliacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}