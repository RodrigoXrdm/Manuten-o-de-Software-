package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.enums.StatusAtendimento;

public interface PeriogramaRepository extends JpaRepository<Periograma, Integer> {
	
	List<Periograma> findAllByAtendimentoStatusAndAtendimentoPacienteOrderByAtendimentoDataDesc(StatusAtendimento status, Paciente paciente);

	Periograma findByAtendimentoId(Integer atendimentoId);
}
