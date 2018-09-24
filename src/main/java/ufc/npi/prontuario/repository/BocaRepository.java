package ufc.npi.prontuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Boca;
import ufc.npi.prontuario.model.Paciente;

public interface BocaRepository extends JpaRepository<Boca, Integer>{
	public Boca findByPaciente(@Param("paciente") Paciente paciente);
}
