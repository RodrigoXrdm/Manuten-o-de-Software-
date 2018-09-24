package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.enums.NumeroDente;

public interface DenteRepository  extends JpaRepository<Dente, Integer>{

	@Query("SELECT COUNT(*) FROM Dente d WHERE d.paciente.id = :pacienteId and d.presente = true")
	public Integer getQtdDentesPresenteFromPaciente (@Param("pacienteId") Integer pacienteId);
	
	public Dente findByNumeroAndPaciente(NumeroDente numeroDente, Paciente paciente);
	
	List<Dente> getByPaciente(Paciente paciente);
}
