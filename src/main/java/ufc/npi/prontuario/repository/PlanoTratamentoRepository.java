package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.model.enums.StatusPlanoTratamento;

public interface PlanoTratamentoRepository extends JpaRepository<PlanoTratamento, Integer> {

	List<PlanoTratamento> findAllByPaciente(Paciente paciente);

	List<PlanoTratamento> findByClinica(Disciplina clinica);

	List<PlanoTratamento> findByClinicaAndStatus(Disciplina disciplina, StatusPlanoTratamento status);

	Integer countByPacienteAndClinicaAndStatusIn(Paciente paciente, Disciplina clinica,
			List<StatusPlanoTratamento> statuses);

}
