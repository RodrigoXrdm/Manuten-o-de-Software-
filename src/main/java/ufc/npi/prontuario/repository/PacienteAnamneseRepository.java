package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PacienteAnamnese;

public interface PacienteAnamneseRepository extends JpaRepository<PacienteAnamnese, Integer> {

	@Query("SELECT pa FROM PacienteAnamnese pa WHERE pa.paciente.id = :#{#paciente.id})")
	List<PacienteAnamnese> buscarPorPaciente(@Param("paciente") Paciente paciente);

}
