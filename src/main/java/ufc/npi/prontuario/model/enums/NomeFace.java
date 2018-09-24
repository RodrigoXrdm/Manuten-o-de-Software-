package ufc.npi.prontuario.model.enums;

public enum NomeFace {

	L("Lingual/Palatal"), D("Distal"), O("Oclusal"), M("Mesial"), V("Vestibular");

	private String descricao;

	private NomeFace(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
