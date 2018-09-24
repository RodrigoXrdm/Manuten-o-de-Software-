package ufc.npi.prontuario.model.enums;

public enum StatusAtendimento {
	EM_ANDAMENTO("Em andamento"), REALIZADO("Aguardando validação"), VALIDADO("Finalizado");

	private String descricao;

	private StatusAtendimento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}