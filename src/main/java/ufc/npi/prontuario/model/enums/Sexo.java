package ufc.npi.prontuario.model.enums;

public enum Sexo {
	M("Masculino"), F("Feminino"), I("Ignorado");

	private String descricao;

	private Sexo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao.toUpperCase();
	}
}