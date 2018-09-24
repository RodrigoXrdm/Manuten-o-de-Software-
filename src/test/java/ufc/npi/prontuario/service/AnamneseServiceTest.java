package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_EXCLUIR_ANAMNESE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_NOME_ANAMNESE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Anamnese.Status;
import ufc.npi.prontuario.model.Pergunta;
import ufc.npi.prontuario.model.Pergunta.TiposPerguntas;
import ufc.npi.prontuario.repository.PerguntaRepository;

@DatabaseSetup(AnamneseServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { AnamneseServiceTest.DATASET })
public class AnamneseServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-anamnese.xml";

	@Autowired
	private AnamneseService anamneseService;

	@Autowired
	private PerguntaRepository perguntaRepository;

	@Test
	public void testBuscarTudo() {
		List<Anamnese> anamneses = anamneseService.buscarTudo();
		assertEquals(3, anamneses.size());
	}

	@Test
	public void testBuscarTodasAnamnesesFinalizadas() {
		List<Anamnese> anamneses = anamneseService.buscarTodasFinalizadas();
		assertEquals(2, anamneses.size());
	}

	@Test
	public void testBuscarAnamnesePorId() {
		Anamnese anamneseValida = anamneseService.buscarPorId(2);
		assertNotNull(anamneseValida);

		assertEquals((Integer) 2, anamneseValida.getId());
		assertEquals("Anamnese 2", anamneseValida.getNome());
		assertEquals("Descrição da anamnese 2", anamneseValida.getDescricao());
		assertEquals(1, anamneseValida.getPerguntas().size());

		assertEquals((Integer) 4, anamneseValida.getPerguntas().get(0).getId());
		assertEquals("Texto da pergunta 4", anamneseValida.getPerguntas().get(0).getTexto());
		assertEquals(TiposPerguntas.TEXTO, anamneseValida.getPerguntas().get(0).getTipo());
		assertEquals((Integer) 1, anamneseValida.getPerguntas().get(0).getOrdem());
		assertEquals(anamneseValida, anamneseValida.getPerguntas().get(0).getAnamnese());
	}

	@Test
	public void testBuscarAnamneseInexistentePorId() {
		Anamnese anamneseInvalida = anamneseService.buscarPorId(4);
		assertNull(anamneseInvalida);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarAnamneseSemCamposObrigatorios() {
		Anamnese anamnese1 = new Anamnese();
		anamnese1.setDescricao("Descrição da anamnese Nova");
		try {
			anamneseService.salvar(anamnese1);
			fail("A anamnese não deveria ter sido cadastrada");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		Anamnese anamnese2 = new Anamnese();
		anamnese2.setNome("Anamnese Nova");
		try {
			anamneseService.salvar(anamnese2);
			fail("A anamnese não deveria ter sido cadastrada");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		anamnese1 = new Anamnese();
		anamnese1.setDescricao("Descrição da anamnese Nova");
		anamnese1.setNome("");
		try {
			anamneseService.salvar(anamnese1);
			fail("A anamnese não deveria ter sido cadastrada");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}

		anamnese2 = new Anamnese();
		anamnese2.setDescricao("");
		anamnese2.setNome("Anamnese Nova");
		try {
			anamneseService.salvar(anamnese2);
			fail("A anamnese não deveria ter sido cadastrada");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarAnamneseComNomeRepetido() {
		Anamnese anamnese = new Anamnese();
		anamnese.setNome("anamnese 1");
		anamnese.setDescricao("Descrição da anamnese Nova");
		try {
			anamneseService.salvar(anamnese);
			fail("A anamnese não deveria ter sido cadastrada");
		} catch (ProntuarioException e) {
			assertEquals(ERROR_NOME_ANAMNESE, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarAnamneseSemPerguntas() {
		Anamnese anamnese = new Anamnese();
		anamnese.setNome("Anamnese Nova");
		anamnese.setDescricao("Descrição da anamnese Nova");
		anamnese.setStatus(Status.EM_ANDAMENTO);

		try {
			anamneseService.salvar(anamnese);
			List<Anamnese> listaAnamneses = anamneseService.buscarTudo();
			Anamnese anamnese2 = listaAnamneses.get(listaAnamneses.size() - 1);
			assertEquals((Integer) 6, anamnese2.getId());
			assertEquals("Anamnese Nova", anamnese2.getNome());
			assertEquals("Descrição da anamnese Nova", anamnese2.getDescricao());
			assertEquals(0, anamnese2.getPerguntas().size());
		} catch (ProntuarioException e) {
			fail("A anamnese deveria ter sido cadastrada");
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarAnamneseComPerguntas() {
		Anamnese anamnese = new Anamnese();
		anamnese.setNome("Anamnese de teste");
		anamnese.setDescricao("Descrição da anamnese de teste");
		anamnese.setStatus(Status.EM_ANDAMENTO);

		Pergunta pergunta1 = new Pergunta();
		pergunta1.setAnamnese(anamnese);
		pergunta1.setTexto("Texto da pergunta 1?");
		pergunta1.setTipo(TiposPerguntas.TEXTO);

		Pergunta pergunta2 = new Pergunta();
		pergunta2.setAnamnese(anamnese);
		pergunta2.setTexto("Texto da pergunta 2?");
		pergunta2.setTipo(TiposPerguntas.SIM_OU_NAO);

		Pergunta pergunta3 = new Pergunta();
		pergunta3.setAnamnese(anamnese);
		pergunta3.setTexto("Texto da pergunta 3?");
		pergunta3.setTipo(TiposPerguntas.TEXTO_E_SIM_OU_NAO);

		try {
			anamneseService.salvar(anamnese);
			anamneseService.salvarPergunta(pergunta1, 4);
			anamneseService.salvarPergunta(pergunta2, 4);
			anamneseService.salvarPergunta(pergunta3, 4);

			Anamnese anamnese2 = anamneseService.buscarPorId(4);

			assertEquals((Integer) 4, anamnese2.getId());
			assertEquals("Anamnese de teste", anamnese2.getNome());
			assertEquals("Descrição da anamnese de teste", anamnese2.getDescricao());
			assertEquals(Status.EM_ANDAMENTO, anamnese2.getStatus());
			assertEquals(3, anamnese2.getPerguntas().size());
			assertEquals(TiposPerguntas.TEXTO, anamnese2.getPerguntas().get(0).getTipo());
			assertEquals((Integer) 1, anamnese2.getPerguntas().get(0).getOrdem());
			assertEquals(TiposPerguntas.SIM_OU_NAO, anamnese2.getPerguntas().get(1).getTipo());
			assertEquals((Integer) 2, anamnese2.getPerguntas().get(1).getOrdem());
			assertEquals(TiposPerguntas.TEXTO_E_SIM_OU_NAO, anamnese2.getPerguntas().get(2).getTipo());
			assertEquals((Integer) 3, anamnese2.getPerguntas().get(2).getOrdem());
		} catch (ProntuarioException e) {
			fail("A anamnese deveria ter sido cadastrada");
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarPerguntaEmAnamnese() {
		Pergunta pergunta1 = new Pergunta();
		pergunta1.setTexto("Texto da pergunta 1");
		pergunta1.setTipo(TiposPerguntas.SIM_OU_NAO);

		Pergunta pergunta2 = new Pergunta();
		pergunta2.setTexto("Texto da pergunta 2");
		pergunta2.setTipo(TiposPerguntas.TEXTO);

		Pergunta pergunta3 = new Pergunta();
		pergunta3.setTexto("Texto da pergunta 3");
		pergunta3.setTipo(TiposPerguntas.TEXTO_E_SIM_OU_NAO);

		// Anamnese já possui uma pergunta.
		Anamnese anamnese = anamneseService.buscarPorId(2);

		assertEquals(1, anamnese.getPerguntas().size());

		anamneseService.salvarPergunta(pergunta1, 2);
		anamneseService.salvarPergunta(pergunta2, 2);
		anamneseService.salvarPergunta(pergunta3, 2);

		Anamnese anamnese2 = anamneseService.buscarPorId(2);
		assertEquals(4, anamnese2.getPerguntas().size());
		assertEquals(TiposPerguntas.TEXTO, anamnese2.getPerguntas().get(0).getTipo());
		assertEquals((Integer) 1, anamnese2.getPerguntas().get(0).getOrdem());
		assertEquals(TiposPerguntas.SIM_OU_NAO, anamnese2.getPerguntas().get(1).getTipo());
		assertEquals((Integer) 2, anamnese2.getPerguntas().get(1).getOrdem());
		assertEquals(TiposPerguntas.TEXTO, anamnese2.getPerguntas().get(2).getTipo());
		assertEquals((Integer) 3, anamnese2.getPerguntas().get(2).getOrdem());
		assertEquals(TiposPerguntas.TEXTO_E_SIM_OU_NAO, anamnese2.getPerguntas().get(3).getTipo());
		assertEquals((Integer) 4, anamnese2.getPerguntas().get(3).getOrdem());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarPerguntaEmAnamneseFinalizada() {
		Pergunta pergunta1 = new Pergunta();
		pergunta1.setTexto("Texto da pergunta 1");
		pergunta1.setTipo(TiposPerguntas.SIM_OU_NAO);

		Pergunta pergunta2 = new Pergunta();
		pergunta2.setTexto("Texto da pergunta 2");
		pergunta2.setTipo(TiposPerguntas.TEXTO);

		Pergunta pergunta3 = new Pergunta();
		pergunta3.setTexto("Texto da pergunta 3");
		pergunta3.setTipo(TiposPerguntas.TEXTO_E_SIM_OU_NAO);

		anamneseService.salvarPergunta(pergunta1, 3);
		anamneseService.salvarPergunta(pergunta2, 3);
		anamneseService.salvarPergunta(pergunta3, 3);

		Anamnese anamnese = anamneseService.buscarPorId(3);
		assertEquals(1, anamnese.getPerguntas().size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testExcluirPerguntaDeAnamnese() {
		Anamnese anamnese = new Anamnese();
		anamnese.setNome("Anamnese Teste");
		anamnese.setDescricao("Descrição Anamnese Teste");
		try {
			anamneseService.salvar(anamnese);
		} catch (ProntuarioException e) {
			fail("Deveria ter salvo a anamnese.");
		}

		Integer anamneseId = anamneseService.buscarTudo().get(anamneseService.buscarTudo().size() - 1).getId();

		anamnese = anamneseService.buscarPorId(anamneseId);
		assertEquals("Anamnese Teste", anamnese.getNome());
		assertEquals("Descrição Anamnese Teste", anamnese.getDescricao());
		assertEquals(Status.EM_ANDAMENTO, anamnese.getStatus());
		assertEquals(0, anamnese.getPerguntas().size());

		Pergunta pergunta1 = new Pergunta();
		pergunta1.setTexto("Texto da pergunta nova 1");
		pergunta1.setTipo(TiposPerguntas.TEXTO);

		Pergunta pergunta2 = new Pergunta();
		pergunta2.setTexto("Texto da pergunta nova 2");
		pergunta2.setTipo(TiposPerguntas.TEXTO_E_SIM_OU_NAO);

		Pergunta pergunta3 = new Pergunta();
		pergunta3.setTexto("Texto da pergunta nova 3");
		pergunta3.setTipo(TiposPerguntas.SIM_OU_NAO);

		anamneseService.salvarPergunta(pergunta1, anamnese.getId());
		anamneseService.salvarPergunta(pergunta2, anamnese.getId());
		anamneseService.salvarPergunta(pergunta3, anamnese.getId());

		anamnese = anamneseService.buscarPorId(anamneseId);
		assertEquals(3, anamnese.getPerguntas().size());

		Integer idPergunta1 = anamnese.getPerguntas().get(0).getId();
		Integer idPergunta2 = anamnese.getPerguntas().get(1).getId();
		Integer idPergunta3 = anamnese.getPerguntas().get(2).getId();

		Pergunta pergunta = perguntaRepository.findOne(idPergunta1);
		anamneseService.excluirPergunta(pergunta, anamnese);
		anamnese = anamneseService.buscarPorId(anamneseId);
		assertEquals(2, anamnese.getPerguntas().size());
		assertEquals(idPergunta2, anamnese.getPerguntas().get(0).getId());
		assertEquals((Integer) 1, anamnese.getPerguntas().get(0).getOrdem());
		assertEquals(TiposPerguntas.TEXTO_E_SIM_OU_NAO, anamnese.getPerguntas().get(0).getTipo());
		assertEquals("Texto da pergunta nova 2", anamnese.getPerguntas().get(0).getTexto());
		assertEquals(idPergunta3, anamnese.getPerguntas().get(1).getId());
		assertEquals((Integer) 2, anamnese.getPerguntas().get(1).getOrdem());
		assertEquals(TiposPerguntas.SIM_OU_NAO, anamnese.getPerguntas().get(1).getTipo());
		assertEquals("Texto da pergunta nova 3", anamnese.getPerguntas().get(1).getTexto());
		assertNull(perguntaRepository.findOne(idPergunta1));

		pergunta = perguntaRepository.findOne(idPergunta2);
		anamneseService.excluirPergunta(pergunta, anamnese);
		anamnese = anamneseService.buscarPorId(anamneseId);
		assertEquals(1, anamnese.getPerguntas().size());
		assertEquals(idPergunta3, anamnese.getPerguntas().get(0).getId());
		assertEquals((Integer) 1, anamnese.getPerguntas().get(0).getOrdem());
		assertEquals(TiposPerguntas.SIM_OU_NAO, anamnese.getPerguntas().get(0).getTipo());
		assertEquals("Texto da pergunta nova 3", anamnese.getPerguntas().get(0).getTexto());
		assertNull(perguntaRepository.findOne(idPergunta2));

		pergunta = anamnese.getPerguntas().get(0);
		anamneseService.excluirPergunta(pergunta, anamnese);
		anamnese = anamneseService.buscarPorId(anamneseId);
		assertEquals(0, anamnese.getPerguntas().size());
		assertNull(perguntaRepository.findOne(idPergunta3));
	}

	@Test
	public void testExcluirPerguntaDeAnamneseFinalizada() {
		Anamnese anamnese = anamneseService.buscarPorId(3);
		Pergunta pergunta = anamnese.getPerguntas().get(0);

		anamneseService.excluirPergunta(pergunta, anamnese);

		Pergunta pergunta2 = perguntaRepository.getOne(3);
		assertNotNull(pergunta2);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverAnamneseSemPerguntas() {
		Anamnese anamnese = new Anamnese();
		anamnese.setNome("Anamnese 4");
		anamnese.setDescricao("Descrição da anamnese 4");
		anamnese.setStatus(Status.EM_ANDAMENTO);
		try {
			anamneseService.salvar(anamnese);
			List<Anamnese> listaAnamnese = anamneseService.buscarTudo();
			anamnese = listaAnamnese.get(listaAnamnese.size() - 1);
			Integer idAnamnese = anamnese.getId();
			anamneseService.remover(anamnese);
			anamnese = anamneseService.buscarPorId(idAnamnese);
			assertNull(anamnese);
		} catch (ProntuarioException e) {
			fail("A anamnese deveria ter sido excluída.");
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverAnamneseComPerguntas() {
		try {
			Anamnese anamnese = anamneseService.buscarPorId(2);
			anamneseService.remover(anamnese);
			anamnese = anamneseService.buscarPorId(2);
			assertNull(anamnese);
			assertNull(perguntaRepository.findOne(4));
		} catch (ProntuarioException e) {
			fail("A anamnese deveria ter sido excluída.");
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverAnamneseFinalizada() {
		Anamnese anamnese = anamneseService.buscarPorId(1);
		try {
			anamneseService.remover(anamnese);
		} catch (ProntuarioException e) {
			assertEquals(ERROR_EXCLUIR_ANAMNESE, e.getMessage());
			anamnese = anamneseService.buscarPorId(1);
			assertNotNull(anamnese);
			assertEquals(2, anamnese.getPerguntas().size());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testFinalizarAnamnese() {
		Anamnese anamnese1 = anamneseService.buscarPorId(2);
		anamneseService.finalizar(anamnese1);

		Anamnese anamnese2 = anamneseService.buscarPorId(2);
		assertEquals(Status.FINALIZADA, anamnese2.getStatus());

		Pergunta pergunta1 = new Pergunta();
		pergunta1.setTexto("Texto da pergunta 1");
		pergunta1.setTipo(TiposPerguntas.SIM_OU_NAO);

		anamneseService.salvarPergunta(pergunta1, 3);

		Anamnese anamnese3 = anamneseService.buscarPorId(2);
		assertEquals(1, anamnese3.getPerguntas().size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAlterarOrdemADePerguntasAnamnese() {
		Anamnese anamnese = anamneseService.buscarPorId(2);

		Pergunta pergunta1 = anamnese.getPerguntas().get(0);

		Pergunta pergunta2 = new Pergunta();
		pergunta2.setTexto("Texto da pergunta nova 1");
		pergunta2.setTipo(TiposPerguntas.SIM_OU_NAO);

		Pergunta pergunta3 = new Pergunta();
		pergunta3.setTexto("Texto da pergunta nova 2");
		pergunta3.setTipo(TiposPerguntas.TEXTO_E_SIM_OU_NAO);

		anamneseService.salvarPergunta(pergunta2, anamnese.getId());
		anamneseService.salvarPergunta(pergunta3, anamnese.getId());

		anamnese = anamneseService.buscarPorId(2);
		assertEquals(3, anamnese.getPerguntas().size());
		assertEquals((Integer) 1, anamnese.getPerguntas().get(0).getOrdem());
		assertEquals((Integer) 2, anamnese.getPerguntas().get(1).getOrdem());
		assertEquals((Integer) 3, anamnese.getPerguntas().get(2).getOrdem());

		pergunta2 = anamnese.getPerguntas().get(1);
		pergunta3 = anamnese.getPerguntas().get(2);

		anamneseService.alterarOrdemAnamnese(anamnese, pergunta1.getId(), 3);
		anamneseService.alterarOrdemAnamnese(anamnese, pergunta2.getId(), 1);
		anamneseService.alterarOrdemAnamnese(anamnese, pergunta3.getId(), 2);

		assertEquals((Integer) 1, anamnese.getPerguntas().get(0).getOrdem());
		assertEquals((Integer) 2, anamnese.getPerguntas().get(1).getOrdem());
		assertEquals((Integer) 3, anamnese.getPerguntas().get(2).getOrdem());

		anamneseService.alterarOrdemAnamnese(anamnese, pergunta1.getId(), 1);
		anamneseService.alterarOrdemAnamnese(anamnese, pergunta2.getId(), 2);
		anamneseService.alterarOrdemAnamnese(anamnese, pergunta3.getId(), 3);

		assertEquals((Integer) 1, anamnese.getPerguntas().get(0).getOrdem());
		assertEquals((Integer) 2, anamnese.getPerguntas().get(1).getOrdem());
		assertEquals((Integer) 3, anamnese.getPerguntas().get(2).getOrdem());
	}
}
