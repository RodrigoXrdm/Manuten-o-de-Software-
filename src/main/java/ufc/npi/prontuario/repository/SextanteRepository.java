package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Sextante;

public interface SextanteRepository  extends JpaRepository<Sextante, Integer>{
	List<Sextante> findAllByPacienteOrderByNomeAsc(Paciente paciente);
}
