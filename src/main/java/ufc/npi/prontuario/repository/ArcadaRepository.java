package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Arcada;
import ufc.npi.prontuario.model.Paciente;

public interface ArcadaRepository extends JpaRepository<Arcada, Integer>{
	List<Arcada> getByPaciente(Paciente paciente);
}
