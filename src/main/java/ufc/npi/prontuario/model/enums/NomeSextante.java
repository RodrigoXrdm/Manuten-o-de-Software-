package ufc.npi.prontuario.model.enums;

public enum NomeSextante {
	S1("Sextante 1"), S2("Sextante 2"), S3("Sextante 3"), S4("Sextante 4"), S5("Sextante 5"), S6("Sextante 6");

	private String descrição;

	private NomeSextante(String descrição) {
		this.descrição = descrição;
	}

	public String getDescrição() {
		return descrição;
	}

}
