package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_DISCIPLINA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_SALVAR_DISCIPLINA_EXISTENTE;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;

@DatabaseSetup(DisciplinaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { DisciplinaServiceTest.DATASET })
public class DisciplinaServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-disciplina.xml";

	@Autowired
	private DisciplinaService disciplinaService;

	@Test
	public void testFindAll() {
		List<Disciplina> disciplina = disciplinaService.buscarTudo();
		assertEquals(3, disciplina.size());
	}

	@Test
	public void testBuscarPorId() {
		Disciplina disciplina = disciplinaService.buscarPorId(1);
		assertEquals("1", disciplina.getCodigo());

		// Busca por Id Inexistente

		assertEquals(null, disciplinaService.buscarPorId(4));
	}

	@Test
	public void testSalvar() {

		Disciplina disciplina = new Disciplina();
		disciplina.setCodigo("111");
		disciplina.setNome("Disciplina4");
		disciplina.setId(4);

		try {
			disciplinaService.salvar(disciplina);
			assertEquals(disciplina, disciplinaService.buscarPorId(4));
		} catch (ProntuarioException e1) {
			fail("A disciplina deveria ter sido cadastrada");
		}

		// teste salvar com código já cadastrado
		Disciplina disciplina2 = new Disciplina();
		disciplina2.setCodigo("2");
		disciplina2.setNome("Disc 4");
		try {
			disciplinaService.salvar(disciplina2);
			fail("Não pode ser possível salvar com código já existente");
		} catch (ProntuarioException e) {
		}

		// teste salvar com nome já cadastrado
		Disciplina disciplina3 = new Disciplina();
		disciplina3.setCodigo("4");
		disciplina3.setNome("disciplina1");
		try {
			disciplinaService.salvar(disciplina3);
			fail("Não pode ser possível salvar com nome já existente");
		} catch (ProntuarioException e) {
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarDisciplinaSemCamposObrigatorios() throws ProntuarioException {
		Disciplina disciplina = new Disciplina();
		disciplina.setNome("");
		disciplina.setCodigo("QXD0039");
		try {
			disciplinaService.salvar(disciplina);
			fail("Deveria ter lançado exceção pois a disciplina está sem nome.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}

		disciplina = new Disciplina();
		disciplina.setNome("IHC");
		disciplina.setCodigo("");
		try {
			disciplinaService.salvar(disciplina);
			fail("Deveria ter lançado exceção pois a disciplina está sem código.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverDisciplinaPorId() throws ProntuarioException {
		Disciplina disciplina = disciplinaService.buscarPorId(3);
		disciplinaService.removerDisciplina(disciplina.getId());
		assertEquals(null, disciplinaService.buscarPorId(3));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverDisciplinaAssociadaComTurma() {
		try {
			disciplinaService.removerDisciplina(1);
			fail("Deveria ter lançado exceção pois a disciplina está associada a turma.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_EXCLUIR_DISCIPLINA);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarDisciplina() throws ProntuarioException {
		Disciplina disciplina = disciplinaService.buscarPorId(1);
		disciplina.setNome("Interface Humano-Computador");
		disciplina.setCodigo("QXD0038");

		disciplinaService.atualizar(disciplina);

		disciplina = disciplinaService.buscarPorId(1);
		assertEquals("QXD0038", disciplina.getCodigo());
		assertEquals("Interface Humano-Computador", disciplina.getNome());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarDisciplinaSemCamposObrigatorios() throws ProntuarioException {
		Disciplina disciplina = disciplinaService.buscarPorId(1);
		disciplina.setNome("");
		try {
			disciplinaService.atualizar(disciplina);
			fail("Deveria ter lançado exceção pois a disciplina está sem nome.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}

		disciplina = disciplinaService.buscarPorId(2);
		disciplina.setCodigo("");
		try {
			disciplinaService.atualizar(disciplina);
			fail("Deveria ter lançado exceção pois a disciplina está sem código.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_CAMPOS_OBRIGATORIOS);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarDisciplinaComMesmoNome() throws ProntuarioException {
		Disciplina disciplina = disciplinaService.buscarPorId(1);
		disciplina.setNome("disciplina2");
		try {
			disciplinaService.atualizar(disciplina);
			fail("Deveria ter lançado exceção pois a disciplina está com nome repetido.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_SALVAR_DISCIPLINA_EXISTENTE);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarDisciplinaComMesmoCodigo() throws ProntuarioException {
		Disciplina disciplina = disciplinaService.buscarPorId(2);
		disciplina.setCodigo("1");
		try {
			disciplinaService.atualizar(disciplina);
			fail("Deveria ter lançado exceção pois a disciplina está com código repetido.");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_SALVAR_DISCIPLINA_EXISTENTE);
		}
	}
}
