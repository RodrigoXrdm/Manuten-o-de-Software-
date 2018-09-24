package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.model.Arcada;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Sextante;

public interface SextanteService {
	List<Sextante> criarSextantes(Paciente paciente, Arcada arcada);
	List<Sextante> findAllByPacienteOrderByNomeAsc(Paciente paciente);
}
