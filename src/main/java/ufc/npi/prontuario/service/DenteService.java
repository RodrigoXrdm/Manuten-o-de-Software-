package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Sextante;

public interface DenteService {
	List<Dente> getByPaciente(Paciente paciente);
	List<Dente> criarDentes(Paciente paciente, Sextante sextante);
}
