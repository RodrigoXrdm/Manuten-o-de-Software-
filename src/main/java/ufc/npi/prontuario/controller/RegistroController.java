package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_REGISTRO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_REGISTRO_LOG;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.prontuario.model.Registro;
import ufc.npi.prontuario.service.PacienteService;
import ufc.npi.prontuario.service.RegistroService;
import ufc.npi.prontuario.service.UsuarioService;

@Controller
@RequestMapping("/registro")
public class RegistroController {

	@Autowired
	RegistroService registroService;

	@Autowired
	PacienteService pacienteService;

	@Autowired
	UsuarioService usuarioService;

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/listar")
	public ModelAndView listarLogAuditoria() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_REGISTRO_LOG);
		List<Registro> registros = registroService.buscarResumoRegistros();
		modelAndView.addObject("logs", registros);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/detalhes/{registroId}")
	public ModelAndView visualizarDetalhesRegistro(@PathVariable("registroId") Registro registro) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_REGISTRO);
		modelAndView.addObject("registro", registro);
		modelAndView.addObject("observacao", registroService.formatarObservacaoHtml(registro.getObservacao()));
		modelAndView.addObject("usuario", usuarioService.buscarPorId(registro.getIdUsuario()));
		modelAndView.addObject("paciente", pacienteService.buscarPorId(registro.getIdPaciente()));

		return modelAndView;
	}

}
