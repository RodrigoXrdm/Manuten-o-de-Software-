package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_SALVAR_TIPO_PROCEDIMENTO_EXISTENTE;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoProcedimento;

@DatabaseSetup(TipoProcedimentoServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { TipoProcedimentoServiceTest.DATASET })
public class TipoProcedimentoServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-tipoProcedimento.xml";

	@Autowired
	private TipoProcedimentoService tipoProcedimentoService;

	@Test
	public void testbuscarTudo() {
		List<TipoProcedimento> tiposProcedimentos = tipoProcedimentoService.buscarTudo();
		assertEquals(3, tiposProcedimentos.size());
	}

	@Test
	public void testBuscarById() {

		TipoProcedimento tipoProcedimento = tipoProcedimentoService.buscarPorId(2);
		assertEquals("0307020070", tipoProcedimento.getCodIdentificador());
		assertEquals("Pulpotomia dentária", tipoProcedimento.getNome());
		assertEquals("Pulpotomia dentária", tipoProcedimento.getDescricao());

		// Busca por ID Inexistente
		assertEquals(null, tipoProcedimentoService.buscarPorId(222));
	}

	@Test
	public void testBuscarTipoProcedimentoQueContemTermoEmSeuNome() {

		List<TipoProcedimento> tiposProcedimentos = tipoProcedimentoService.buscarPorNome("dentária");
		assertEquals(2, tiposProcedimentos.size());

		// Busca por Termo Inexistente
		assertEquals(0, tipoProcedimentoService.buscarPorNome("cárie").size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarTipoProcedimento() {
		TipoProcedimento tipoProcedimento = new TipoProcedimento();
		tipoProcedimento.setCodIdentificador("0307010015");
		tipoProcedimento.setDescricao("Capeamento pulpar");
		tipoProcedimento.setNome("Capeamento pulpar");

		try {
			tipoProcedimentoService.salvar(tipoProcedimento);
			assertEquals(tipoProcedimento, tipoProcedimentoService.buscarPorId(4));
			assertEquals("0307010015", tipoProcedimento.getCodIdentificador());
			assertEquals("Capeamento pulpar", tipoProcedimento.getNome());
			assertEquals("Capeamento pulpar", tipoProcedimento.getDescricao());
		} catch (ProntuarioException e) {
			fail("Deveria ter cadastrado o tipo de procedimento");
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarTipoProcedimentoComMesmoNome() {
		TipoProcedimento tipoProcedimento = new TipoProcedimento();
		tipoProcedimento.setCodIdentificador("0307010015");
		tipoProcedimento.setNome("Pulpotomia dentária");
		tipoProcedimento.setDescricao("Pulpotomia dentária");

		try {
			tipoProcedimentoService.salvar(tipoProcedimento);
			fail("Não deveria ter cadastrado o tipo de procedimento pois o nem já existe.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_SALVAR_TIPO_PROCEDIMENTO_EXISTENTE, e.getMessage());
		}
	}

	@Test
	public void testSalvarTipoProcedimentoSemCamposObrigatorios() {
		TipoProcedimento tipoProcedimento = new TipoProcedimento();
		tipoProcedimento.setCodIdentificador("");
		tipoProcedimento.setNome("Pulpotomia dentária");
		tipoProcedimento.setDescricao("Pulpotomia dentária");

		try {
			tipoProcedimentoService.salvar(tipoProcedimento);
			fail("Não deveria ter cadastrado o tipo de procedimento sem o código identificador.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoProcedimento.setCodIdentificador("0307010015");
		tipoProcedimento.setNome("");
		tipoProcedimento.setDescricao("Pulpotomia dentária");

		try {
			tipoProcedimentoService.salvar(tipoProcedimento);
			fail("Não deveria ter cadastrado o tipo de procedimento sem o nome.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoProcedimento.setCodIdentificador("0307010015");
		tipoProcedimento.setNome("Abscesso periapical");
		tipoProcedimento.setDescricao("");

		try {
			tipoProcedimentoService.salvar(tipoProcedimento);
			fail("Não deveria ter cadastrado o tipo de procedimento sem a descrição.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarTipoProcedimento() {
		TipoProcedimento tipoProcedimento = tipoProcedimentoService.buscarPorId(1);
		tipoProcedimento.setCodIdentificador("0307010015");
		tipoProcedimento.setDescricao("Capeamento pulpar");
		tipoProcedimento.setNome("Capeamento pulpar");

		try {
			tipoProcedimentoService.atualizar(tipoProcedimento);
		} catch (ProntuarioException e) {
			fail("Deveria ter alterado o tipo de procedimento");
		}
	}

	@Test
	public void testAtualizarTipoProcedimentoSemCamposObrigatorios() {
		TipoProcedimento tipoProcedimento = tipoProcedimentoService.buscarPorId(1);
		tipoProcedimento.setCodIdentificador("");
		tipoProcedimento.setNome("Abscesso periapical");
		tipoProcedimento.setDescricao("Abscesso periapical");

		try {
			tipoProcedimentoService.atualizar(tipoProcedimento);
			fail("Não deveria ter atualizado o tipo de procedimento sem o código.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoProcedimento.setCodIdentificador("0307020070");
		tipoProcedimento.setNome("");
		tipoProcedimento.setDescricao("Capeamento pulpar");

		try {
			tipoProcedimentoService.atualizar(tipoProcedimento);
			fail("Não deveria ter atualizado o tipo de procedimento sem o código.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoProcedimento.setCodIdentificador("0307020070");
		tipoProcedimento.setNome("Capeamento pulpar");
		tipoProcedimento.setDescricao("");

		try {
			tipoProcedimentoService.atualizar(tipoProcedimento);
			fail("Não deveria ter atualizado o tipo de procedimento sem o código.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		tipoProcedimento.setId(null);
		tipoProcedimento.setCodIdentificador("0307020070");
		tipoProcedimento.setNome("Capeamento pulpar");
		tipoProcedimento.setDescricao("Capeamento pulpar");

		try {
			tipoProcedimentoService.atualizar(tipoProcedimento);
			fail("Não deveria ter atualizado o tipo de procedimento sem id.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarTipoProcedimentoComMesmoNome() {
		TipoProcedimento tipoProcedimento = tipoProcedimentoService.buscarPorId(1);
		tipoProcedimento.setCodIdentificador("0307020070");
		tipoProcedimento.setNome("Pulpotomia dentária");
		tipoProcedimento.setDescricao("Pulpotomia dentária");

		try {
			tipoProcedimentoService.atualizar(tipoProcedimento);
			fail("Não deveria ter alterado o tipo de procedimento pois o nome já existe.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_SALVAR_TIPO_PROCEDIMENTO_EXISTENTE, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverTipoProcedimento() throws Exception {
		TipoProcedimento tipoProcedimento = tipoProcedimentoService.buscarPorId(1);
		tipoProcedimentoService.remover(tipoProcedimento.getId());
		tipoProcedimento = tipoProcedimentoService.buscarPorId(1);
		assertNull(tipoProcedimento);
	}
}
