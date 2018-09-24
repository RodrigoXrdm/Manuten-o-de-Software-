package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ALTERAR_SENHA;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;

@DatabaseSetup(UsuarioServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { UsuarioServiceTest.DATASET })
public class UsuarioServiceTest extends AbstractServiceTest {

	public static final String DATASET = "/database-tests-usuario.xml";

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private TokenService tokenService;

	@Test
	public void testBuscarPorId() {
		Usuario usuario = usuarioService.buscarPorId(1);
		assertEquals("aluno 1", usuario.getNome());
		assertEquals("aluno1@email.com", usuario.getEmail());
		assertEquals("1111", usuario.getMatricula());
		assertEquals("$2a$08$7ZoeQ8pkNKGKZOsSwIcc3u/.qpodWCajWkHxoejTc/m5xbCv/O3pq", usuario.getSenha());
	}

	@Test
	public void buscarPorEmail() {
		String email = "servidor1@email.com";

		Usuario usuario = usuarioService.buscarPorEmail(email);

		assertEquals(email, usuario.getEmail());
	}

	@Test
	public void alterarSenha() {
		Usuario usuario = usuarioService.buscarPorEmail("servidor1@email.com");

		try {
			usuarioService.alterarSenha(usuario.getId(), "1234", "12345");
		} catch (ProntuarioException e) {
			e.printStackTrace();
		}

		assertTrue(new BCryptPasswordEncoder().matches("12345", usuario.getSenha()));

		try {
			usuarioService.alterarSenha(usuario.getId(), "4321", "12345");
		} catch (ProntuarioException e) {
			assertEquals(e.getMessage(), ERRO_ALTERAR_SENHA);
		}
	}

	@Test
	public void recuperarSenhaUsuarioInvalido() {
		String email = "nomeEmail@email.com.br";

		usuarioService.recuperarSenha(email);

		Usuario usuario = usuarioService.buscarPorEmail(email);

		assertNull(tokenService.buscarPorUsuario(usuario));
	}

	@Test
	public void recuperarSenhaUsuarioComSolicitacaoAtiva() {
		String email = "servidor1@email.com";
		Usuario usuario = usuarioService.buscarPorEmail(email);

		String aux = tokenService.buscarPorUsuario(usuario).getToken();

		usuarioService.recuperarSenha(email);

		assertEquals(aux, tokenService.buscarPorUsuario(usuario).getToken());
	}

	@Test
	public void recuperarSenhaUsuarioSemSolicitacaoAtiva() {
		String email = "servidor2@email.com";

		usuarioService.recuperarSenha(email);

		Usuario usuario = usuarioService.buscarPorEmail(email);

		assertNotNull(tokenService.buscarPorUsuario(usuario));
	}

	@Test
	public void novaSenha() {
		Token token = tokenService.buscar("6a8b39b2-361b-4842-a80f-48b234f7918d");

		Usuario usuario = token.getUsuario();

		usuarioService.novaSenha(token, "12345");

		assertTrue(new BCryptPasswordEncoder().matches("12345", usuario.getSenha()));
		assertFalse(tokenService.existe("6a8b39b2-361b-4842-a80f-48b234f7918d"));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAtualizarUsuario() throws ProntuarioException {
		Usuario usuario = usuarioService.buscarPorId(1);
		String nome = "Aluno Novo";
		String email = "aluno_novo@email.com";
		String matricula = "9999";
		String senha = usuario.getSenha();

		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setMatricula(matricula);

		usuarioService.alterarDados(1, nome, email, matricula);

		usuario = usuarioService.buscarPorId(1);
		assertEquals(nome, usuario.getNome());
		assertEquals(email, usuario.getEmail());
		assertEquals(matricula, usuario.getMatricula());
		assertEquals(senha, usuario.getSenha());
	}
}
