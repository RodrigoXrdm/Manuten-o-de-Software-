package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ATENDENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_TRATAMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_CADASTRO_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_TRATAMENTOS;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_INDEX;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.enums.StatusPlanoTratamento;
import ufc.npi.prontuario.service.DisciplinaService;
import ufc.npi.prontuario.service.PlanoTratamentoService;

@Controller
@RequestMapping("/paciente/{idPaciente}/tratamento")
public class PlanoTratamentoController {

	@Autowired
	private DisciplinaService disciplinaService;

	@Autowired
	private PlanoTratamentoService tratamentoService;

	@GetMapping("/listar")
	public ModelAndView listarTratamentos(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_TRATAMENTOS);
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("tratamentos", paciente.getTratamentos());
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ATENDENTE)
	@GetMapping("/cadastrar")
	public ModelAndView cadastroForm(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView mv = new ModelAndView(PAGINA_CADASTRO_PLANO_TRATAMENTO);

		List<StatusPlanoTratamento> statuses = Arrays.asList(StatusPlanoTratamento.EM_ESPERA,
				StatusPlanoTratamento.EM_ANDAMENTO, StatusPlanoTratamento.CONCLUIDO,
				StatusPlanoTratamento.INTERROMPIDO);
		mv.addObject("statuses", statuses);
		mv.addObject("paciente", paciente);
		mv.addObject("clinicas", disciplinaService.buscarTudo());
		mv.addObject("tratamento", new PlanoTratamento());
		mv.addObject("acao", "cadastrar");
		return mv;
	}

	@PreAuthorize(PERMISSAO_ATENDENTE)
	@PostMapping("/cadastrar")
	public ModelAndView cadastrar(PlanoTratamento planoTratamento, @PathVariable("idPaciente") Paciente paciente,
			@RequestParam("responsavel") Servidor responsavel, RedirectAttributes attributes) {
		String redirectTratamentos = REDIRECT_INDEX + "paciente/" + paciente.getId() + "/tratamento/listar";
		ModelAndView mv = new ModelAndView(redirectTratamentos);
		try {
			tratamentoService.salvar(planoTratamento, responsavel, paciente);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_TRATAMENTO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return mv;
	}

	@PreAuthorize(PERMISSAO_ATENDENTE)
	@GetMapping("/{tratamento}/editar")
	public ModelAndView editarForm(@PathVariable("tratamento") PlanoTratamento planoTratamento,
			RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(PAGINA_CADASTRO_PLANO_TRATAMENTO);

		List<StatusPlanoTratamento> statuses = Arrays.asList(StatusPlanoTratamento.EM_ESPERA,
				StatusPlanoTratamento.EM_ANDAMENTO, StatusPlanoTratamento.CONCLUIDO,
				StatusPlanoTratamento.INTERROMPIDO);
		mv.addObject("statuses", statuses);
		mv.addObject("clinicas", disciplinaService.buscarTudo());
		mv.addObject("paciente", planoTratamento.getPaciente());
		mv.addObject("tratamento", planoTratamento);
		mv.addObject("acao", "editar");
		return mv;
	}

	@PreAuthorize(PERMISSAO_ATENDENTE)
	@PostMapping("/editar")
	public ModelAndView editar(PlanoTratamento planoTratamento, RedirectAttributes attributes) {
		final String redirectTratamentos = REDIRECT_INDEX + "paciente/" + planoTratamento.getPaciente().getId()
				+ "/tratamento/listar";
		ModelAndView mv = new ModelAndView(redirectTratamentos);

		try {
			tratamentoService.editar(planoTratamento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EDITAR_TRATAMENTO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}

		return mv;
	}

	@PreAuthorize(PERMISSAO_ATENDENTE)
	@GetMapping("/{tratamento}/excluir")
	public ModelAndView excluir(@PathVariable("tratamento") PlanoTratamento planoTratamento,
			@PathVariable("idPaciente") Paciente paciente, RedirectAttributes attributes) {
		final String redirectTratamentos = REDIRECT_INDEX + "paciente/" + paciente.getId() + "/tratamento/listar";
		ModelAndView mv = new ModelAndView(redirectTratamentos);

		try {
			tratamentoService.excluirPlanoTratamento(planoTratamento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_TRATAMENTO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}

		mv.addObject("paciente", paciente);
		mv.addObject("tratamentos", paciente.getTratamentos());

		return mv;
	}

}
