package ufc.npi.prontuario.model.enums;

public enum StatusPlanoTratamento {
	EM_ESPERA("Em espera"), EM_ANDAMENTO("Em andamento"), CONCLUIDO("Concluído"), INTERROMPIDO("Interrompido");

	private String descricao;

	private StatusPlanoTratamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean isAtivo() {
		return this.name().equalsIgnoreCase("aguardando");
	}
}
