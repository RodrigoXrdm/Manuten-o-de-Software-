package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ALTERAR_SENHA;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.repository.UsuarioRepository;
import ufc.npi.prontuario.service.EmailService;
import ufc.npi.prontuario.service.TokenService;
import ufc.npi.prontuario.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private EmailService emailService;

	@Override
	public void alterarSenha(Integer usuarioId, String senhaAtual, String novaSenha) throws ProntuarioException {
		Usuario aux = usuarioRepository.findOne(usuarioId);

		if (new BCryptPasswordEncoder().matches(senhaAtual, aux.getSenha())) {
			aux.setSenha(novaSenha);
			aux.encodePassword();
		} else {
			throw new ProntuarioException(ERRO_ALTERAR_SENHA);
		}

		usuarioRepository.save(aux);
	}

	@Override
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
	@Override
	public Usuario buscarPorId(Integer id) {
		return usuarioRepository.getOne(id);
	}

	@Override
	public void recuperarSenha(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario != null) {
			Token token = null;
			token = tokenService.buscarPorUsuario(usuario);

			if (token == null) {
				token = new Token();

				token.setUsuario(usuario);

				do {
					token.setToken(UUID.randomUUID().toString());
				} while (tokenService.existe(token.getToken()));

				tokenService.salvar(token);
			}

			emailService.emailRecuperacaoSenha(token);
		}

	}

	@Override
	public void novaSenha(Token token, String senha) {
		if (token != null) {
			Usuario usuario = token.getUsuario();
			usuario.setSenha(senha);
			usuario.encodePassword();

			usuarioRepository.save(usuario);

			tokenService.deletar(token);
		}
	}

	@Override
	public void alterarDados(Integer id, String nome, String email, String matricula) {
		Usuario usuario = usuarioRepository.getOne(id);

		usuario.setNome(nome);
		usuario.setMatricula(matricula);
		usuario.setEmail(email);

		usuarioRepository.save(usuario);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getPassword(), usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
