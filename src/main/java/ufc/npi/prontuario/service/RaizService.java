package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Raiz;

public interface RaizService {

	List<Raiz> getByDente(Dente dente);
	List<Raiz> criarRaizes(Paciente paciente, Dente dente);
}
