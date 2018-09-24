package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.*;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoPatologia;

@DatabaseSetup(TipoPatologiaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { TipoPatologiaServiceTest.DATASET })
public class TipoPatologiaServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-tipoPatologia.xml";

	@Autowired
	private TipoPatologiaService tipoPatologiaService;

	@Test
	public void testbuscarTodasTipoPatologias() {
		List<TipoPatologia> tipoPatologia = tipoPatologiaService.buscarTudo();
		assertEquals(3, tipoPatologia.size());
	}

	@Test
	public void testBuscarTipoPatologiaById() {
		TipoPatologia tipoPatologia = tipoPatologiaService.buscarPorId(1);
		assertEquals("K.23", tipoPatologia.getCodigo());
		assertEquals("Alteração de cor", tipoPatologia.getNome());
		assertEquals("Alteração de cor", tipoPatologia.getDescricao());

		// Busca por ID Inexistente
		assertEquals(null, tipoPatologiaService.buscarPorId(222));
	}

	@Test
	public void testBuscarTipoPatologiaQueContemTermoEmSeuNome() {
		List<TipoPatologia> tipoPatologia = tipoPatologiaService.buscarPorNome("abscesso");
		assertEquals(2, tipoPatologia.size());

		tipoPatologia = tipoPatologiaService.buscarPorNome("alteração");
		assertEquals(1, tipoPatologia.size());

		// Busca por Termo Inexistente
		assertTrue(tipoPatologiaService.buscarPorNome("restaurações").isEmpty());
	}

	@Test
	public void testBuscarUmTipoPatologiaPorNome() {
		TipoPatologia tipoPatologia = tipoPatologiaService.buscarUmTipoPatologiaPorNome("Alteração de cor");
		assertEquals("K.23", tipoPatologia.getCodigo());
		assertEquals("Alteração de cor", tipoPatologia.getNome());
		assertEquals("Alteração de cor", tipoPatologia.getDescricao());

		// Busca por Nome Inexistente
		assertEquals(null, tipoPatologiaService.buscarUmTipoPatologiaPorNome("Reabsorção óssea vertical"));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarTipoPatologia() {
		TipoPatologia tipoPatologia = new TipoPatologia();
		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setDescricao("Abscesso periapical");
		tipoPatologia.setNome("Abscesso periapical");

		try {
			tipoPatologiaService.salvar(tipoPatologia);
			tipoPatologia = tipoPatologiaService.buscarPorId(4);
			assertEquals(tipoPatologia, tipoPatologiaService.buscarPorId(4));
			assertEquals("K04.7", tipoPatologia.getCodigo());
			assertEquals("Abscesso periapical", tipoPatologia.getNome());
			assertEquals("Abscesso periapical", tipoPatologia.getDescricao());
		} catch (ProntuarioException e) {
			fail("Deveria ter cadastrado o tipo de patologia");
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarTipoPatologiaComMesmoNome() {
		TipoPatologia tipoPatologia = new TipoPatologia();
		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setNome("Alteração de cor");
		tipoPatologia.setDescricao("Abscesso periapical");

		try {
			tipoPatologiaService.salvar(tipoPatologia);
			fail("Não deveria ter cadastrado o tipo de patologia pois o nem já existe.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_SALVAR_TIPO_PATOLOGIA_EXISTENTE, e.getMessage());
		}
	}

	@Test
	public void testSalvarTipoPatologiaSemCamposObrigatorios() {
		TipoPatologia tipoPatologia = new TipoPatologia();
		tipoPatologia.setCodigo("");
		tipoPatologia.setNome("Abscesso periapical");
		tipoPatologia.setDescricao("Abscesso periapical");

		try {
			tipoPatologiaService.salvar(tipoPatologia);
			fail("Não deveria ter cadastrado o tipo de patologia sem o código.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setNome("");
		tipoPatologia.setDescricao("Abscesso periapical");

		try {
			tipoPatologiaService.salvar(tipoPatologia);
			fail("Não deveria ter cadastrado o tipo de patologia sem o nome.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setNome("Abscesso periapical");
		tipoPatologia.setDescricao("");

		try {
			tipoPatologiaService.salvar(tipoPatologia);
			fail("Não deveria ter cadastrado o tipo de patologia sem a descrição.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarTipoPatologia() {
		TipoPatologia tipoPatologia = tipoPatologiaService.buscarPorId(1);
		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setDescricao("Abscesso periapical");
		tipoPatologia.setNome("Abscesso periapical");

		try {
			tipoPatologiaService.atualizar(tipoPatologia);
		} catch (ProntuarioException e) {
			fail("Deveria ter alterado o tipo de patologia");
		}
	}

	@Test
	public void testAtualizarTipoPatologiaSemCamposObrigatorios() {
		TipoPatologia tipoPatologia = tipoPatologiaService.buscarPorId(1);
		tipoPatologia.setCodigo("");
		tipoPatologia.setNome("Abscesso periapical");
		tipoPatologia.setDescricao("Abscesso periapical");

		try {
			tipoPatologiaService.atualizar(tipoPatologia);
			fail("Não deveria ter atualizado o tipo de patologia sem o código.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setNome("");
		tipoPatologia.setDescricao("Abscesso periapical");

		try {
			tipoPatologiaService.atualizar(tipoPatologia);
			fail("Não deveria ter atualizado o tipo de patologia sem o código.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setNome("Abscesso periapical");
		tipoPatologia.setDescricao("");

		try {
			tipoPatologiaService.atualizar(tipoPatologia);
			fail("Não deveria ter atualizado o tipo de patologia sem o código.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoPatologia.setId(null);
		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setNome("Abscesso periapical");
		tipoPatologia.setDescricao("Abscesso periapical");

		try {
			tipoPatologiaService.atualizar(tipoPatologia);
			fail("Não deveria ter atualizado o tipo de patologia sem id.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarTipoPatologiaComMesmoNome() {
		TipoPatologia tipoPatologia = tipoPatologiaService.buscarPorId(1);
		tipoPatologia.setCodigo("K04.7");
		tipoPatologia.setNome("Abscesso periodontal");
		tipoPatologia.setDescricao("Abscesso periapical");

		try {
			tipoPatologiaService.atualizar(tipoPatologia);
			fail("Não deveria ter alterado o tipo de patologia pois o nome já existe.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_SALVAR_TIPO_PATOLOGIA_EXISTENTE, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverTipoPatologia() throws Exception {
		TipoPatologia tipoPatologia = tipoPatologiaService.buscarPorId(1);
		tipoPatologiaService.remover(tipoPatologia.getId());
		tipoPatologia = tipoPatologiaService.buscarPorId(1);
		assertNull(tipoPatologia);
	}
}
