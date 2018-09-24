package ufc.npi.prontuario.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.TipoEstrutura;

@Entity
public class Furca extends Estrutura {

	@ManyToMany(mappedBy = "furcas")
	@JsonIgnore
	private List<Raiz> raizes;

	@Enumerated(EnumType.STRING)
	private NomeFace face;

	public Furca() {
		super();
		this.raizes = new ArrayList<Raiz>();
		super.setTipoEstrutura(TipoEstrutura.FURCA);
	}

	public List<Raiz> getRaizes() {
		return raizes;
	}
	
	public boolean addRaiz(Raiz raiz){
		return this.raizes.add(raiz);
	}

	public void setRaizes(List<Raiz> raizes) {
		this.raizes = raizes;
	}

	public NomeFace getFace() {
		return face;
	}

	public void setFace(NomeFace face) {
		this.face = face;
	}

}
