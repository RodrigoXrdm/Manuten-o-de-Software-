package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
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
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.enums.Papel;

@DatabaseSetup(ServidorServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ServidorServiceTest.DATASET })
public class ServidorServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-servidor.xml";

	@Autowired
	private ServidorService servidorService;

	@Test
	public void testBuscarTudo() {
		assertEquals(5, servidorService.buscarProfessores().size());
	}

	@Test
	public void testBuscarPorId() {
		Servidor servidor = servidorService.buscarPorId(1);

		assertEquals(servidor.getId(), (Integer) 1);
		assertEquals(servidor.getEmail(), "servidor1@email.com");
		assertEquals(servidor.getNome(), "Servidor 1");
		assertEquals(servidor.getMatricula(), "555555");
		assertEquals(servidor.getSenha(), "1234");

		// test buscar por um id não existente
		assertEquals(null, servidorService.buscarPorId(10));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarServidor() throws ProntuarioException {
		List<Papel> papeis = new ArrayList<>();
		papeis.add(Papel.ADMINISTRACAO);
		Servidor servidor = new Servidor();
		servidor.setId(8);
		servidor.setEmail("servidor3@email.com");
		servidor.setNome("Servidor 3");
		servidor.setMatricula("555554");
		servidor.setPapeis(papeis);

		servidorService.salvar(servidor);
		servidor = servidorService.buscarPorId(50);
		assertEquals("servidor3@email.com", servidor.getEmail());
		assertEquals("Servidor 3", servidor.getNome());
		assertEquals("555554", servidor.getMatricula());
		assertEquals(Papel.ADMINISTRACAO, servidor.getPapeis().get(0));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarServidorSemNome() {
		Servidor servidor = new Servidor();
		servidor.setEmail("servidor1@email.com");
		servidor.setNome("");
		servidor.setMatricula("555559");
		servidor.setSenha("1234");
		servidor.encodePassword();
		try {
			servidorService.salvar(servidor);
			fail("Deveria ter lançado exceção para servidor sem nome.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_ADICIONAR_SERVIDOR_VAZIO, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarServidorSemPapeis() {
		List<Papel> papeis = new ArrayList<>();
		Servidor servidor = new Servidor();
		servidor.setEmail("servidor8@email.com");
		servidor.setNome("Servidor 4");
		servidor.setMatricula("888888");
		servidor.setSenha("1234");
		servidor.encodePassword();
		servidor.setPapeis(papeis);
		try {
			servidorService.salvar(servidor);
			fail("Deveria ter lançado exceção para servidor sem papel.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_ADICIONAR_SERVIDOR_PAPEL, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarServidorComEmailJaExistente() {
		Servidor servidor = new Servidor();
		servidor.setEmail("servidor1@email.com");
		servidor.setNome("Servidor 13");
		servidor.setMatricula("555559");
		servidor.setSenha("1234");
		servidor.encodePassword();
		try {
			servidorService.salvar(servidor);
			fail("Deveria ter lançado exceção para matricula ou email já cadastrados.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_ADICIONAR_SERVIDOR, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarServidorComMatriculaJaExistente() {
		Servidor servidor = new Servidor();
		servidor.setEmail("servidor4@email.com");
		servidor.setNome("Servidor 4");
		servidor.setMatricula("555556");
		servidor.setSenha("1234");
		servidor.encodePassword();

		try {
			servidorService.salvar(servidor);
			fail("Deveria ter lançado exceção para matricula ou email já cadastrados.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_ADICIONAR_SERVIDOR, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarServidor() throws ProntuarioException {
		Servidor servidor = servidorService.buscarPorId(2);
		servidor.setEmail("servidor3@email.com");
		servidor.setNome("Servidor 3");
		servidor.setMatricula("555554");
		servidor.setSenha("1234");

		servidorService.atualizar(servidor);
		servidor = servidorService.buscarPorId(2);

		assertEquals("servidor3@email.com", servidor.getEmail());
		assertEquals("Servidor 3", servidor.getNome());
		assertEquals("555554", servidor.getMatricula());
		assertEquals("1234", servidor.getSenha());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarServidorComEmailJaExistente() {
		Servidor servidor = servidorService.buscarPorId(2);
		servidor.setEmail("servidor1@email.com");
		servidor.setNome("Servidor 13");
		servidor.setMatricula("555559");
		servidor.setSenha("1234");
		servidor.encodePassword();
		try {
			servidorService.atualizar(servidor);
			fail("Deveria ter lançado exceção para matricula ou email já cadastrados.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_ADICIONAR_SERVIDOR, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarServidorComMatriculaJaExistente() {
		Servidor servidor = servidorService.buscarPorId(2);
		servidor.setEmail("servidor4@email.com");
		servidor.setNome("Servidor 4");
		servidor.setMatricula("555555");
		servidor.setSenha("1234");
		servidor.encodePassword();

		try {
			servidorService.atualizar(servidor);
			fail("Deveria ter lançado exceção para matricula ou email já cadastrados.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_ADICIONAR_SERVIDOR, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarServidorSemNome() {
		Servidor servidor = servidorService.buscarPorId(2);
		servidor.setEmail("servidor1@email.com");
		servidor.setNome("");
		servidor.setMatricula("555559");
		servidor.setSenha("1234");
		servidor.encodePassword();
		try {
			servidorService.atualizar(servidor);
			fail("Deveria ter lançado exceção para servidor sem nome.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarServidorSemMatricula() {
		Servidor servidor = servidorService.buscarPorId(2);
		servidor.setEmail("servidor1@email.com");
		servidor.setNome("Servidor 4");
		servidor.setMatricula("");
		servidor.setSenha("1234");
		servidor.encodePassword();
		try {
			servidorService.atualizar(servidor);
			fail("Deveria ter lançado exceção para servidor sem matricula.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarServidorSemEmail() {
		Servidor servidor = servidorService.buscarPorId(2);
		servidor.setEmail("");
		servidor.setNome("Servidor 4");
		servidor.setMatricula("555559");
		servidor.setSenha("1234");
		servidor.encodePassword();
		try {
			servidorService.atualizar(servidor);
			fail("Deveria ter lançado exceção para servidor sem email.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarServidorSemPapeis() {
		List<Papel> papeis = new ArrayList<>();
		Servidor servidor = servidorService.buscarPorId(2);
		servidor.setEmail("servidor8@email.com");
		servidor.setNome("Servidor 4");
		servidor.setMatricula("888888");
		servidor.setSenha("1234");
		servidor.encodePassword();
		servidor.setPapeis(papeis);
		try {
			servidorService.atualizar(servidor);
			fail("Deveria ter lançado exceção para servidor sem papel.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_ADICIONAR_SERVIDOR_PAPEL, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverServidorPorId() throws ProntuarioException {
		servidorService.removerServidor(1);
		assertNull(servidorService.buscarPorId(1));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverServidorAlocadoEmTurma() {
		try {
			servidorService.removerServidor(103);
			fail("Deveria ter lançado exceção pois professor está alocado em turma.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_EXCLUIR_SERVIDOR, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testRemoverServidorProfessorAvaliadorDeAtendimento() {
		try {
			servidorService.removerServidor(102);
			fail("Deveria ter lançado exceção  pois professor está alocado em atendimento.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_EXCLUIR_SERVIDOR, e.getMessage());
		}
	}

}
