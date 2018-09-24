package ufc.npi.prontuario.service;

import static org.junit.Assert.*;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Turma;
import ufc.npi.prontuario.model.enums.Papel;

@DatabaseSetup(TurmaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { TurmaServiceTest.DATASET })
public class TurmaServiceTest extends AbstractServiceTest {

	public static final String DATASET = "/database-tests-turma.xml";

	@Autowired
	private TurmaService turmaService;

	@Autowired
	private DisciplinaService disciplinaService;

	@Autowired
	private ServidorService servidorService;

	@Autowired
	private AlunoService alunoService;

	@Test
	public void testBuscarTudo() {
		assertEquals(3, turmaService.buscarTudo().size());
	}

	@Test
	public void testAlterarStatusTurma() {
		Turma turma = turmaService.buscarPorId(2);
		assertEquals(false, turma.getAtivo());

		turmaService.alterarStatus(turma);
		turma = turmaService.buscarPorId(2);
		assertEquals(true, turma.getAtivo());

		Aluno aluno = alunoService.buscarPorId(1);
		assertEquals(2, turmaService.buscarAtivasPorAluno(aluno).size());

		turmaService.alterarStatus(turma);
		turma = turmaService.buscarPorId(2);
		assertEquals(false, turma.getAtivo());

		aluno = alunoService.buscarPorId(1);
		assertEquals(1, turmaService.buscarAtivasPorAluno(aluno).size());
	}

	@Test
	public void testBuscarAtivasPorAluno() {
		Aluno aluno = alunoService.buscarPorId(1);
		assertEquals(1, turmaService.buscarAtivasPorAluno(aluno).size());
	}

	@Test
	public void testBuscarTurmaPorId() {
		// Busca por Id existente
		Turma turma = turmaService.buscarPorId(1);
		assertEquals("Turma 1", turma.getNome());

		// Busca por Id inexistente
		assertEquals(null, turmaService.buscarPorId(4));
	}

	@Test
	public void testSalvarTurma() throws ProntuarioException {
		Disciplina disciplina = disciplinaService.buscarPorId(1);
		List<Servidor> professores = servidorService.buscarProfessores();
		Turma turma = new Turma();
		turma.setAno(2018);
		turma.setAtivo(true);
		turma.setSemestre(2);
		turma.setDisciplina(disciplina);
		turma.setNome("Turma D");
		turma.setProfessores(professores);

		turmaService.salvar(turma);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarTurmaRepetida() {
		Disciplina disciplina = disciplinaService.buscarPorId(1);
		List<Servidor> professores = servidorService.buscarProfessores();
		Turma turma = new Turma();
		turma.setAno(2016);
		turma.setAtivo(true);
		turma.setSemestre(1);
		turma.setDisciplina(disciplina);
		turma.setNome("Turma 1");
		turma.setProfessores(professores);

		try {
			turmaService.salvar(turma);
		} catch (ProntuarioException e) {
			assertEquals(ERRO_SALVAR_TURMA_EXISTENTE, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAdicionarProfessorTurma() {
		Turma turma = turmaService.buscarPorId(3);
		List<Servidor> servidores = servidorService.buscarProfessores();
		List<Servidor> professores = new ArrayList<Servidor>();
		for (Servidor servidor : servidores) {
			if (servidor.getPapeis().contains(Papel.PROFESSOR)) {
				professores.add(servidor);
			}
		}
		turmaService.adicionarProfessorTurma(turma, professores);

		Turma turmaAux = turmaService.buscarPorId(3);
		assertEquals(2, turmaAux.getProfessores().size());
	}

	@Test
	public void testInscreverAluno() {
		String matricula = "2222";
		Turma turma = turmaService.buscarPorId(3);

		// Inscrever um aluno na turma
		try {
			turmaService.inscreverAluno(turma, matricula);
		} catch (ProntuarioException e) {
			fail("O aluno deveria ter sido cadastrado");
		}
	}

	@Test
	public void testInscreverAlunoJaExistente() {
		String matricula = "2222";
		Turma turma = turmaService.buscarPorId(1);

		// Inscrever um aluno já existente na turma
		try {
			turmaService.inscreverAluno(turma, matricula);
			fail("O aluno não deveria ter sido cadastrado");
		} catch (ProntuarioException e) {
			assertEquals(ERROR_ALUNO_JA_CADASTRADO, e.getMessage());
		}
	}

	@Test
	public void testInscreverAlunoInexistente() {
		String matricula = "99999";
		Turma turma = turmaService.buscarPorId(1);

		try {
			turmaService.inscreverAluno(turma, matricula);
			fail("Aluno inexistente não deveria ter sido cadastrado");
		} catch (ProntuarioException e) {
			assertEquals(ERROR_MATRICULA_INEXISTENTE, e.getMessage());
		}
	}

	@Test
	public void testRemoverInscricaoDeAlunoMatriculado() throws ProntuarioException {
		Turma turma = turmaService.buscarPorId(1);
		Aluno aluno = alunoService.buscarPorId(3);
		turmaService.removerInscricao(turma, aluno);
	}

	@Test
	public void testRemoverInscricaoDeAlunoNaoInscrito() {
		Turma turma = turmaService.buscarPorId(1);
		Aluno aluno = alunoService.buscarPorId(8);

		try {
			turmaService.removerInscricao(turma, aluno);
		} catch (ProntuarioException e) {
			assertEquals(ERROR_INSCRICAO_ALUNO_NAO_ENCONTRADA, e.getMessage());
		}

	}

	@Test
	public void testRemoverInscricaoDeAlunoComAtendimento() {
		Turma turma = turmaService.buscarPorId(1);
		Aluno aluno = alunoService.buscarPorId(2);

		try {
			turmaService.removerInscricao(turma, aluno);
		} catch (ProntuarioException e) {
			assertEquals(ERROR_ALUNO_POSSUI_ATENDIMENTO, e.getMessage());
		}
	}

	@Test
	public void testBuscarProfessores() {
		List<Servidor> professores = new ArrayList<Servidor>();
		assertEquals(2, turmaService.buscarProfessores(professores).size());
	}

	@Test
	public void testBuscarProfessoresPassandoProfessor() {
		List<Servidor> professores = new ArrayList<Servidor>();
		professores.add(servidorService.buscarPorId(101));
		assertEquals(1, turmaService.buscarProfessores(professores).size());
	}

	@Test
	public void testBuscarTurmasPorProfessor() {
		Servidor professor = servidorService.buscarPorId(101);
		assertEquals(1, turmaService.buscarTurmasProfessor(professor).size());
	}

	@Test
	public void testBuscarTurmasPorServidorAdministrador() {
		Servidor servidor = servidorService.buscarPorId(103);
		assertEquals(3, turmaService.buscarTurmas(servidor).size());
	}

	@Test
	public void testBuscarTurmasPorServidorProfessor() {
		Servidor servidor = servidorService.buscarPorId(102);
		assertEquals(2, turmaService.buscarTurmas(servidor).size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverTurma() throws ProntuarioException {
		Turma turma = turmaService.buscarPorId(3);
		turmaService.removerTurma(turma.getId());
		assertEquals(2, turmaService.buscarTudo().size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverTurmaComAlunosMatriculados() {
		Turma turma = turmaService.buscarPorId(2);
		try {
			turmaService.removerTurma(turma.getId());
		} catch (ProntuarioException e) {
			assertEquals(ERRO_EXCLUIR_TURMA_EXCEPTION, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverTurmaComAtendimentosRealizados() {
		Turma turma = turmaService.buscarPorId(1);
		try {
			turmaService.removerTurma(turma.getId());
		} catch (ProntuarioException e) {
			assertEquals(ERRO_EXCLUIR_TURMA_EXCEPTION, e.getMessage());
		}
	}

	@Test
	public void testRemoverProfessorQueNaoPossuiAtendimentos() throws ProntuarioException {
		Turma turma = turmaService.buscarPorId(1);
		Servidor professor = servidorService.buscarPorId(102);
		turmaService.removerProfessor(turma, professor);

		turma = turmaService.buscarPorId(1);
		assertEquals(1, turma.getProfessores().size());
	}

	@Test
	public void testRemoverProfessorQueNaoEstaNaTurma() throws ProntuarioException {
		Turma turma = turmaService.buscarPorId(1);
		Servidor professor = servidorService.buscarPorId(103);
		turmaService.removerProfessor(turma, professor);

		turma = turmaService.buscarPorId(1);
		assertEquals(2, turma.getProfessores().size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverProfessorQuePossuiAtendimentos() {
		try {
			Turma turma = turmaService.buscarPorId(1);
			Servidor professor = servidorService.buscarPorId(101);
			turmaService.removerProfessor(turma, professor);
		} catch (ProntuarioException e) {
			assertEquals(ERROR_PROFESSOR_POSSUI_ATENDIMENTO, e.getMessage());
		}
	}
}