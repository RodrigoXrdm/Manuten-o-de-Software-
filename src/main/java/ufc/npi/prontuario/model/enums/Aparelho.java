package ufc.npi.prontuario.model.enums;

public enum Aparelho {
	FIXO("Aparelho fixo"), MOVEL("Aparelho móvel");

	private String descrição;

	private Aparelho(String descrição) {
		this.descrição = descrição;
	}

	public String getDescrição() {
		return descrição;
	}
}
