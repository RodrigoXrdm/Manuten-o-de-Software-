package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Estrutura;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.enums.StatusAtendimento;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, Integer> {

	@Query("SELECT p FROM Procedimento p WHERE p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND (a.responsavel.id = :#{#aluno.id} OR a.ajudante.id = :#{#aluno.id}  ))")
	List<Procedimento> buscarPorResponsavelOuAjudante(@Param("paciente") Paciente paciente,
			@Param("aluno") Aluno aluno);

	@Query("SELECT p FROM Procedimento p WHERE p.preExistente = :#{#preExistente} AND p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND (a.responsavel.id = :#{#aluno.id} OR a.ajudante.id = :#{#aluno.id}))")
	List<Procedimento> buscarPorResponsavelOuAjudante(@Param("paciente") Paciente paciente, @Param("aluno") Aluno aluno,
			@Param("preExistente") Boolean preExistente);

	@Query("SELECT p FROM Procedimento p WHERE p.preExistente = :#{#preExistente} AND p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND (a.professor.id = :#{#professor.id}))")
	List<Procedimento> findAllByPacienteAndAtendimentoProfessor(@Param("paciente") Paciente paciente,
			@Param("professor") Servidor professor, @Param("preExistente") Boolean preExistente);

	@Query("SELECT p FROM Procedimento p WHERE p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND (a.professor.id = :#{#professor.id}))")
	List<Procedimento> findAllByPacienteAndAtendimentoProfessor(@Param("paciente") Paciente paciente,
			@Param("professor") Servidor professor);

	@Query("SELECT p FROM Procedimento p WHERE p.preExistente = :#{#preExistente} AND p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id})")
	List<Procedimento> buscarPorPaciente(@Param("paciente") Paciente paciente,
			@Param("preExistente") Boolean preExistente);

	@Query("SELECT p FROM Procedimento p WHERE p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id})")
	List<Procedimento> buscarPorPaciente(@Param("paciente") Paciente paciente);

	@Query("SELECT p FROM Procedimento p WHERE p.preExistente = :#{#preExistente} AND p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND a.status = :#{#status})")
	List<Procedimento> buscarPorPacienteEStatus(@Param("paciente") Paciente paciente,
			@Param("status") StatusAtendimento status, @Param("preExistente") Boolean preExistente);

	@Query("SELECT p FROM Procedimento p WHERE p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND a.status = :#{#status})")
	List<Procedimento> buscarPorPacienteEStatus(@Param("paciente") Paciente paciente,
			@Param("status") StatusAtendimento status);

	@Query("SELECT p FROM Procedimento p WHERE p.preExistente = :#{#preExistente} AND p.estrutura.id = :#{#estrutura.id} AND p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND (a.responsavel.id = :#{#aluno.id} OR a.ajudante.id = :#{#aluno.id}) AND a.status = :#{#status})")
	List<Procedimento> findAllByEstruturaAndAlunoAndPre(@Param("estrutura") Estrutura estrutura,
			@Param("aluno") Aluno aluno, @Param("preExistente") Boolean preExistente,
			@Param("status") StatusAtendimento status);

	@Transactional
	@Modifying
	@Query("DELETE FROM Procedimento p WHERE p.estrutura.id = :#{#estrutura.id} AND p.preExistente = :#{#preExistente} AND p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.status = :#{#status} AND (a.responsavel.id = :#{#aluno.id} OR a.ajudante.id = :#{#aluno.id}))")
	void deleteAllByEstruturaAndAlunoAndPreAndStatus(@Param("estrutura") Estrutura estrutura,
			@Param("aluno") Aluno aluno, @Param("preExistente") Boolean preExistente,
			@Param("status") StatusAtendimento status);

	@Transactional
	@Modifying
	@Query("UPDATE Procedimento p SET p.descricao = :descricao WHERE p.id = :id")
	void updateDescricaoById(@Param("descricao") String descricao, @Param("id") int id);

	@Query("SELECT p FROM Procedimento p WHERE p.ficha.id in (SELECT f.id From Ficha f WHERE f.atendimento.id = :id)")
	List<Procedimento> buscarPorAtendimento(Atendimento atendimento);
}
