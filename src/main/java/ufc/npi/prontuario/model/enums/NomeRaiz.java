package ufc.npi.prontuario.model.enums;

public enum NomeRaiz {

	R1("Raiz 1"), R2("Raiz 2"), R3("Raiz 3");

	private String descricao;

	private NomeRaiz(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
