package ufc.npi.prontuario.model.enums;

public enum TipoDente {

	PERMANENTE("Permanente"), DECIDUO("Decíduo");

	private String descrição;

	private TipoDente(String descrição) {
		this.descrição = descrição;
	}

	public String getDescrição() {
		return descrição;
	}

}
