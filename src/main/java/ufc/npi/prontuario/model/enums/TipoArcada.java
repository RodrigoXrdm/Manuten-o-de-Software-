package ufc.npi.prontuario.model.enums;

public enum TipoArcada {
	SUPERIOR("Arcada superior"), INFERIOR("Arcada inferior");

	private String descrição;

	private TipoArcada(String descrição) {
		this.descrição = descrição;
	}

	public String getDescrição() {
		return descrição;
	}
}
