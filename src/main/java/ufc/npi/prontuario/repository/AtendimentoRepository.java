package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Avaliacao;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Turma;
import ufc.npi.prontuario.model.enums.StatusAtendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {

	List<Atendimento> findAllByResponsavelOrAjudanteOrderByDataDesc(Aluno responsavel, Aluno ajudante);

	List<Atendimento> findAllByResponsavelAndTurmaOrAjudanteAndTurma(Aluno responsavel, Turma turma1, Aluno ajudante,
			Turma turma2);

	List<Atendimento> findAllByProfessorAndTurma(Servidor professor, Turma turma);

	List<Atendimento> findAllByProfessor(Servidor professor);

	List<Atendimento> findAllByProfessorAndPaciente(Servidor professor, Paciente paciente);

	@Query("SELECT a FROM Atendimento a WHERE a.paciente.id = :#{#paciente.id} AND (a.responsavel.id = :#{#aluno.id} OR a.ajudante.id = :#{#aluno.id})")
	List<Atendimento> buscarTodosPorPacienteEAluno(@Param("paciente") Paciente paciente, @Param("aluno") Aluno aluno);

	List<Atendimento> findAllByStatusAndPaciente(StatusAtendimento status, Paciente paciente);

	List<Atendimento> findAllByStatusAndPacienteOrderByDataDesc(StatusAtendimento status, Paciente paciente);

	List<Atendimento> findAllByPaciente(Paciente paciente);

	List<Atendimento> findAllByStatusAndPacienteAndIdIsNotIn(StatusAtendimento status, Paciente paciente,
			List<Integer> idAtendimentos);

	Atendimento findByStatusAndPacienteAndResponsavel(StatusAtendimento status, Paciente paciente, Aluno responsavel);

	Atendimento findByStatusAndPaciente(StatusAtendimento status, Paciente paciente);

	// busca todos os atendimentos em aberto do aluno responsavel e do aluno
	// auxiliar para o paciente
	@Query("select at from Atendimento as at where (at.responsavel = :a1 OR at.ajudante = :a1 OR at.responsavel = :a2 OR at.ajudante = :a2) "
			+ "AND at.paciente = :pac AND at.status = :sta")
	List<Atendimento> findAllByResponsavelOrAjudanteExist(@Param("a1") Aluno aluno, @Param("a2") Aluno aluno2,
			@Param("pac") Paciente paciente, @Param("sta") StatusAtendimento status);

	// busca todos os atendimentos em aberto do aluno como responsavel ou auxiliar
	// para o paciente
	@Query("select at from Atendimento as at where (at.responsavel = :alu OR at.ajudante = :alu) "
			+ "AND at.paciente = :pac AND at.status = :sta")
	List<Atendimento> findAllByResponsavelOrAjudanteExist(@Param("alu") Aluno aluno, @Param("pac") Paciente paciente,
			@Param("sta") StatusAtendimento status);

	List<Atendimento> findAllByStatus(StatusAtendimento status);

	List<Atendimento> findAllByAvaliacao_avaliacao(Avaliacao avaliacao);
}