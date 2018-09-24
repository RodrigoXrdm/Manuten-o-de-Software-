package ufc.npi.prontuario.model.enums;

public enum Raca {
	BRANCA("Branca"), 
	PRETA("Preta"), 
	PARDA("Parda"), 
	AMARELA("Amarela"), 
	INDIGENA("Indígena"), 
	SEM_INFORMACAO("Sem Informação");

	private String descricao;

	private Raca(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao.toUpperCase();
	}
}
