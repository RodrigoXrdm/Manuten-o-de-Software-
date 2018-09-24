package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_PERIOGRAMA_CADASTRADO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_PERIOGRAMA;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_PERIOGRAMA;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.enums.Papel;
import ufc.npi.prontuario.model.enums.StatusPeriograma;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.PeriogramaService;
import ufc.npi.prontuario.service.SextanteService;

@Controller
@RequestMapping("/periograma")
public class PeriogramaController {

	@Autowired
	private PeriogramaService periogramaService;

	@Autowired
	private AtendimentoService atendimentoService;

	@Autowired
	private SextanteService sextanteService;

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/paciente/{id}")
	public ModelAndView getPeriogramas(@PathVariable("id") Paciente paciente, String status, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_PERIOGRAMA);

		Atendimento atendimento = atendimentoService.buscarAtendimentoPorStatusEmAndamento(paciente);
		Periograma periograma = periogramaService.buscarPorAtendimento(atendimento);

		List<Atendimento> atendimentos = atendimentoService.buscarAtendimentosValidadosPorDataDesc(paciente);

		Collection<? extends GrantedAuthority> papeis = auth.getAuthorities();

		if (papeis.contains(Papel.ESTUDANTE)) {
			modelAndView.addObject("periogramaJaCadastrado",
					periogramaService.existePeriogramaJaCadastrado(paciente, (Aluno) auth.getPrincipal()));
			modelAndView.addObject("atendimentos", atendimentos);
			modelAndView.addObject("atendimentoAtual", atendimento);
			modelAndView.addObject("paciente", paciente);
			modelAndView.addObject("sextantes", sextanteService.findAllByPacienteOrderByNomeAsc(paciente));
			modelAndView.addObject("periograma", periograma);
		} else if (papeis.contains(Papel.PROFESSOR) || papeis.contains(Papel.ADMINISTRACAO)) {
			modelAndView.addObject("periogramaJaCadastrado", false);
		}

		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/paciente/{id}/novo")
	public ModelAndView getNovoPeriograma(@PathVariable("id") Paciente paciente, String status, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_PERIOGRAMA);

		// List<Periograma> periogramas =
		// periogramaService.buscarPeriogramasValidados(paciente);
		// boolean isAtual = true;

		Atendimento atendimento = atendimentoService.buscarAtendimentoPorStatusEmAndamento(paciente);
		Periograma periograma = periogramaService.buscarPorAtendimento(atendimento);

		List<Atendimento> atendimentos = atendimentoService.buscarAtendimentosValidadosPorDataDesc(paciente);

		modelAndView.addObject("periogramaJaCadastrado",
				periogramaService.existePeriogramaJaCadastrado(paciente, (Aluno) auth.getPrincipal()));
		modelAndView.addObject("atendimentos", atendimentos);
		modelAndView.addObject("atendimentoAtual", atendimento);
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("sextantes", sextanteService.findAllByPacienteOrderByNomeAsc(paciente));
		modelAndView.addObject("periograma", periograma);
		// modelAndView.addObject("isAtual", isAtual);

		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/paciente/{id}/periograma/{atendimentoId}")
	public ModelAndView selectPeriograma(@PathVariable("id") Paciente paciente,
			@PathVariable("atendimentoId") Atendimento atendimento, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_PERIOGRAMA);

		List<Atendimento> atendimentos = atendimentoService.buscarAtendimentosValidadosPorDataDesc(paciente);
		Atendimento atendimentoSelecionado = atendimento;
		atendimentos.remove(atendimentoSelecionado);

		modelAndView.addObject("atendimentos", atendimentos);
		modelAndView.addObject("atendimentoSelecionado", atendimentoSelecionado);
		modelAndView.addObject("paciente", paciente);
		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@PostMapping("/cadastrar-periograma/{idPaciente}")
	public ModelAndView cadastrarPeriograma(@PathVariable("idPaciente") Paciente paciente, Authentication auth,
			RedirectAttributes attributes, @ModelAttribute("status") StatusPeriograma statusPeriograma) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_PERIOGRAMA + paciente.getId() + "/novo");
		try {
			periogramaService.adicionar(paciente, (Aluno) auth.getPrincipal(), statusPeriograma);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_PERIOGRAMA_CADASTRADO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}

		return modelAndView;
	}

}