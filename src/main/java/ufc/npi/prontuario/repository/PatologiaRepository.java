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
import ufc.npi.prontuario.model.Ficha;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.model.enums.StatusAtendimento;

public interface PatologiaRepository extends JpaRepository<Patologia, Integer> {

	@Query("SELECT p FROM Patologia p WHERE p.ficha.id in (SELECT f.id From Ficha f WHERE f.atendimento.id = :id)")
	List<Patologia> buscarPorAtendimento(@Param("id") Atendimento atendimento);
	
	@Query("SELECT p FROM Patologia p WHERE p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND (a.responsavel.id = :#{#aluno.id} OR a.ajudante.id = :#{#aluno.id}  ))")
	List<Patologia> buscarPorResponsavelOuAjudante(@Param("paciente") Paciente paciente, @Param("aluno") Aluno aluno);
	
	@Query("SELECT p FROM Patologia p WHERE p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND (a.professor.id = :#{#professor.id}))")
	List<Patologia> findAllByPacienteAndAtendimentoProfessor(@Param("paciente") Paciente paciente, @Param("professor") Servidor professor);
	
	@Query("SELECT p FROM Patologia p WHERE p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id})")
	List<Patologia> buscarPorPaciente(@Param("paciente") Paciente paciente);
	
	@Query("SELECT p FROM Patologia p WHERE p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.paciente.id = :#{#paciente.id} AND a.status = :#{#status})")
	List<Patologia> buscarPorPacienteEStatus(@Param("paciente") Paciente paciente, @Param("status") StatusAtendimento status);
	
	@Query("SELECT p FROM Patologia p WHERE p.estrutura in (SELECT e.id FROM Estrutura e WHERE e.id = :#{#estrutura.id}) AND p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.status = :#{#status} AND (a.responsavel.id = :#{#aluno.id} OR a.ajudante.id = :#{#aluno.id}))")
	List<Patologia> buscarPatologiaEstruturaAndamento(@Param("estrutura") Estrutura estrutura,  @Param("aluno") Aluno aluno, @Param("status") StatusAtendimento status);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Patologia p WHERE p.estrutura.id = :#{#estrutura.id} AND p.ficha.id in (SELECT f.id From Ficha f, Atendimento a WHERE f.atendimento.id = a.id AND a.status = :#{#status} AND (a.responsavel.id = :#{#aluno.id} OR a.ajudante.id = :#{#aluno.id}))")
	void apagarPorEstruturaEAlunoEStatus(@Param("estrutura") Estrutura estrutura, @Param("aluno") Aluno aluno, @Param("status") StatusAtendimento status);

	@Transactional
	@Modifying
	@Query("UPDATE Patologia p SET p.descricao = :descricao WHERE p.id = :id")
	void updateDescricaoById(@Param("descricao") String descricao, @Param("id") int id);
	
	Patologia findByEstruturaAndFicha(Estrutura estrutura, Ficha ficha); 
	
	Patologia findByEstruturaAndFichaAndTipo(Estrutura estrutura, Ficha ficha, TipoPatologia tipo);
}
