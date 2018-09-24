package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_ALUNO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ATUALIZAR_ALUNO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_ALUNO;

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

@DatabaseSetup(AlunoServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { AlunoServiceTest.DATASET })
public class AlunoServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-aluno.xml";

	@Autowired
	private AlunoService alunoService;

	@Test
	public void testBuscarTudo() {
		List<Aluno> alunos = alunoService.buscarTudo();
		assertEquals(4, alunos.size());
	}

	@Test
	public void testBuscarPorId() {
		Aluno aluno;
		aluno = alunoService.buscarPorId(1);
		long id = aluno.getId();
		assertEquals(1, id);
		assertEquals("aluno 1", aluno.getNome());
		assertEquals("aluno1@email.com", aluno.getEmail());
		assertEquals("1111", aluno.getMatricula());
		assertEquals((Integer) 2013, aluno.getAnoIngresso());
		assertEquals((Integer) 1, aluno.getSemestreIngresso());

		// buscar por id inexistente
		aluno = alunoService.buscarPorId(4);
		assertNull(aluno);
	}

	@Test
	public void testBuscarPorMatricula() {
		Aluno aluno;
		aluno = alunoService.buscarPorMatricula("3333");
		assertEquals("aluno 3", aluno.getNome());
		assertEquals("3333", aluno.getMatricula());
		assertEquals("aluno3@email.com", aluno.getEmail());
		assertEquals((Integer) 2014, aluno.getAnoIngresso());
		assertEquals((Integer) 1, aluno.getSemestreIngresso());

		// buscar por matricula inexistente
		aluno = alunoService.buscarPorMatricula("4444");
		assertNull(aluno);
	}

	@Test
	public void testBuscarPorEmail() {
		Aluno aluno;
		aluno = alunoService.buscarPorEmail("aluno2@email.com");
		assertEquals("aluno 2", aluno.getNome());
		assertEquals("aluno2@email.com", aluno.getEmail());
		assertEquals("2222", aluno.getMatricula());
		assertEquals((Integer) 2013, aluno.getAnoIngresso());
		assertEquals((Integer) 2, aluno.getSemestreIngresso());

		// buscar por matricula inexistente
		aluno = alunoService.buscarPorEmail("aluno4@email.com");
		assertNull(aluno);
	}

	@Test
	public void testBuscarAjudantes() {
		Aluno ajudante = new Aluno();
		ajudante.setId(1);
		List<Aluno> alunos = alunoService.buscarAjudantes(1, ajudante);
		assertEquals(2, alunos.size());

		// Verifica se o ajudante está na lista
		assertEquals(false, alunos.contains(ajudante));

		// buscar por ajudante inexistente
		ajudante.setId(4);
		alunos = alunoService.buscarAjudantes(2, ajudante);
		assertNull(alunos);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarAluno() throws ProntuarioException {
		Aluno aluno = new Aluno();
		aluno.setEmail("aluno@email.com");
		aluno.setMatricula("308020");
		aluno.setNome("Aluno 1");
		aluno.setSenha("1234");
		aluno.encodePassword();
		aluno.setAnoIngresso(2010);
		aluno.setSemestreIngresso(1);

		int quantidade = alunoService.buscarTudo().size();
		alunoService.salvar(aluno);
		Aluno alunoNovo = alunoService.buscarPorId(aluno.getId());

		assertNotEquals(quantidade, alunoService.buscarTudo().size());
		assertEquals(aluno.getId(), alunoNovo.getId());
		assertEquals(aluno.getEmail(), alunoNovo.getEmail());
		assertEquals(aluno.getMatricula(), alunoNovo.getMatricula());
		assertEquals(aluno.getNome(), alunoNovo.getNome());
		assertEquals(aluno.getSenha(), alunoNovo.getSenha());
		assertEquals(aluno.getAnoIngresso(), alunoNovo.getAnoIngresso());
		assertEquals(aluno.getSemestreIngresso(), alunoNovo.getSemestreIngresso());
	}

	@Test
	public void testSalvarAlunoComMatriculaJaExistente() {
		Aluno aluno = new Aluno();
		aluno.setNome("Aluno 101");
		aluno.setMatricula("3333");
		aluno.setEmail("aluno101@email.com");
		aluno.setAnoIngresso(2016);
		aluno.setSemestreIngresso(1);
		try {
			alunoService.salvar(aluno);
			fail("Deveria ter lançado exceção para matricula já cadastrada.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_ADICIONAR_ALUNO);
		}
	}

	@Test
	public void testSalvarAlunoComEmailJaExistente() {
		Aluno aluno = new Aluno();
		aluno.setNome("Aluno 401");
		aluno.setMatricula("0403210");
		aluno.setEmail("aluno3@email.com");
		aluno.setAnoIngresso(2016);
		aluno.setSemestreIngresso(1);
		try {
			alunoService.salvar(aluno);
			fail("Deveria ter lançado exceção para email já cadastrado.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_ADICIONAR_ALUNO);
		}
	}

	@Test
	public void testSalvarAlunoSemCamposObrigatoriosPreenchidos() {
		Aluno aluno = new Aluno();
		aluno.setNome("");
		aluno.setMatricula("465412");
		aluno.setEmail("teste@email.com");
		aluno.setAnoIngresso(2016);
		try {
			alunoService.salvar(aluno);
			fail("Deveria ter lançado exceção pois o nome está vazio.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}

		aluno.setNome("Aluno 465");
		aluno.setMatricula("");
		aluno.setEmail("teste@email.com");
		aluno.setAnoIngresso(2016);
		try {
			alunoService.salvar(aluno);
			fail("Deveria ter lançado exceção pois a matrícula está vazia.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}

		aluno.setNome("Aluno 465");
		aluno.setMatricula("465412");
		aluno.setEmail("");
		aluno.setAnoIngresso(2016);
		try {
			alunoService.salvar(aluno);
			fail("Deveria ter lançado exceção pois o email está vazio.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}

		aluno.setNome("Aluno 465");
		aluno.setMatricula("465412");
		aluno.setEmail("teste@email.com");
		aluno.setAnoIngresso(null);
		try {
			alunoService.salvar(aluno);
			fail("Deveria ter lançado exceção pois o ano de ingresso está nulo.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarAluno() throws ProntuarioException {
		Aluno aluno = alunoService.buscarPorId(1);
		String nome = "Aluno Novo";
		String email = "aluno_novo@email.com";
		String matricula = "9999";
		String senha = aluno.getSenha();
		int anoIngresso = 2018;
		int semestreIngresso = 2;

		aluno.setNome(nome);
		aluno.setEmail(email);
		aluno.setMatricula(matricula);
		aluno.setAnoIngresso(anoIngresso);
		aluno.setSemestreIngresso(semestreIngresso);

		alunoService.atualizar(aluno);

		aluno = alunoService.buscarPorId(1);
		assertEquals(nome, aluno.getNome());
		assertEquals(email, aluno.getEmail());
		assertEquals(matricula, aluno.getMatricula());
		assertEquals(senha, aluno.getSenha());
		assertEquals((Integer) anoIngresso, aluno.getAnoIngresso());
		assertEquals((Integer) semestreIngresso, aluno.getSemestreIngresso());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarAlunoComMatriculaJaExistente() {
		Aluno aluno = alunoService.buscarPorId(2);
		aluno.setMatricula("3333");
		try {
			alunoService.atualizar(aluno);
			fail("Deveria ter lançado exceção para matricula já cadastrada.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_ATUALIZAR_ALUNO);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarAlunoComEmailJaExistente() {
		Aluno aluno = alunoService.buscarPorId(1);
		aluno.setEmail("aluno2@email.com");
		try {
			alunoService.atualizar(aluno);
			fail("Deveria ter lançado exceção para email já cadastrado.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_ATUALIZAR_ALUNO);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarAlunoSemCamposObrigatoriosPreenchidos() {
		Aluno aluno = alunoService.buscarPorId(1);
		aluno.setNome("");
		try {
			alunoService.atualizar(aluno);
			fail("Deveria ter lançado exceção pois o nome está vazio.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}

		aluno = alunoService.buscarPorId(2);
		aluno.setMatricula("");
		try {
			alunoService.atualizar(aluno);
			fail("Deveria ter lançado exceção pois a matrícula está vazia.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}

		aluno = alunoService.buscarPorId(3);
		aluno.setEmail("");
		try {
			alunoService.atualizar(aluno);
			fail("Deveria ter lançado exceção pois o email está vazio.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}

		aluno = alunoService.buscarPorId(3);
		aluno.setAnoIngresso(null);
		try {
			alunoService.atualizar(aluno);
			fail("Deveria ter lançado exceção pois o ano de ingresso está nulo.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverAlunoPorId() throws ProntuarioException {
		alunoService.removerAluno(8);
		Aluno aluno = alunoService.buscarPorId(8);
		assertNull(aluno);
	}

	@Test
	public void testRemoverQuePossuiAtendimentos() {
		try {
			alunoService.removerAluno(1);
			fail("Deveria ter lançado exceção pois aluno possui atendimentos.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_EXCLUIR_ALUNO);
		}
	}

	@Test
	public void testRemoverAlunoMatriculadoEmTurma() {
		try {
			alunoService.removerAluno(3);
			fail("Deveria ter lançado exceção pois aluno está matriculado em uma turma.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_EXCLUIR_ALUNO);
		}
	}
}
