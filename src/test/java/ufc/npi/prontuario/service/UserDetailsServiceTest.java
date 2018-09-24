package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.PESSOA_NAO_ENCONTRADA;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.service.impl.UserDetailsService;

@DatabaseSetup(UserDetailsServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { UserDetailsServiceTest.DATASET })
public class UserDetailsServiceTest extends AbstractServiceTest {

	public static final String DATASET = "/database-tests-usuario.xml";

	@Autowired
	private UserDetailsService userDetailsService;

	@Test
	public void testCarregarUsuairoPorUsernameExistente() {
		String email = "aluno1@email.com";

		UserDetails userDetails = userDetailsService.loadUserByUsername(email);

		assertEquals(email, userDetails.getUsername());
		assertEquals("$2a$08$7ZoeQ8pkNKGKZOsSwIcc3u/.qpodWCajWkHxoejTc/m5xbCv/O3pq", userDetails.getPassword());

		String matricula = "3333";

		userDetails = userDetailsService.loadUserByUsername(matricula);

		assertEquals("aluno3@email.com", userDetails.getUsername());
		assertEquals("$2a$08$7ZoeQ8pkNKGKZOsSwIcc3u/.qpodWCajWkHxoejTc/m5xbCv/O3pq", userDetails.getPassword());
	}

	@Test
	public void testCarregarUsuairoPorUsernameInexistente() {
		String userName = "aluno888@email.com";

		try {
			userDetailsService.loadUserByUsername(userName);
			fail("Deveria ter lançado exceção pois não existe usuário com esse username.");
		} catch (Exception e) {
			assertEquals(PESSOA_NAO_ENCONTRADA, e.getMessage());
		}

	}

}
