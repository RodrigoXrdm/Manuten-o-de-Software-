package ufc.npi.prontuario.model.enums;

public enum StatusPeriograma {
	EXAME_INICIAL("Exame Inicial"), REAVALIACAO("Reavaliação"), MANUTENCAO("Manutenção");

	private String descricao;

	private StatusPeriograma(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}