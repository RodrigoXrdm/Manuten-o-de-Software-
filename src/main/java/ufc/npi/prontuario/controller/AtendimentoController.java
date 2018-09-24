package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE_VERIFICACAO_ESTUDANTE;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_PROFESSOR_VERIFICACAO_PROFESSOR;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_AVALIAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_PATOLOGIA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_PROCEDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_FINALIZAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_VALIDAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_ADICIONAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_ATENDIMENTO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_DETALHES_ATENDIMENTO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_INDEX;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_ODONTOGRAMA;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.AvaliacaoAtendimento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.model.Registro.Acao;
import ufc.npi.prontuario.model.enums.Papel;
import ufc.npi.prontuario.model.enums.StatusAtendimento;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.PatologiaService;
import ufc.npi.prontuario.service.ProcedimentoService;
import ufc.npi.prontuario.service.RegistroService;
import ufc.npi.prontuario.service.TurmaService;

@Controller
@RequestMapping("/atendimento")
public class AtendimentoController {

	@Autowired
	private AtendimentoService atendimentoService;

	@Autowired
	private AlunoService alunoService;

	@Autowired
	private TurmaService turmaService;

	@Autowired
	private PatologiaService patologiaService;

	@Autowired
	private ProcedimentoService procedimentoService;

	@Autowired
	private RegistroService registroService;

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idAtendimento}")
	public ModelAndView getDetalhesAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
			Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_ATENDIMENTO);
		atendimentoService.adicionarAvaliacaoAtendimento(atendimento);

		if (atendimento.isVisivel(auth.getName()) || auth.getAuthorities().contains(Papel.ADMINISTRACAO)) {
			List<Patologia> patologias = patologiaService.buscarPorAtendimento(atendimento);
			List<Procedimento> procedimentos = procedimentoService.buscarPorAtendimento(atendimento);
			modelAndView.addObject("patologias", patologias);
			modelAndView.addObject("procedimentos", procedimentos);
			modelAndView.addObject("atendimento", atendimento);
			modelAndView.addObject("paciente", atendimento.getPaciente());
			modelAndView.addObject("avaliacaoAtendimento", atendimento.getAvaliacao());
		} else {
			modelAndView.setViewName(REDIRECT_INDEX);
		}
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@GetMapping("/{id}/cadastrar")
	public ModelAndView formularioCadastrarAtendimento(@PathVariable("id") Paciente paciente, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_ATENDIMENTO);
		Aluno aluno = (Aluno) auth.getPrincipal();
		modelAndView.addObject("atendimento", new Atendimento());
		modelAndView.addObject("turmas", turmaService.buscarAtivasPorAluno(aluno));
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("action", "cadastrar");
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/cadastrar")
	public ModelAndView cadastrarAtendimento(Atendimento atendimento, @RequestParam("paciente") Paciente paciente,
			RedirectAttributes attributes, Authentication auth) {
		atendimento.setPaciente(paciente);
		ModelAndView modelAndView = new ModelAndView("redirect:/paciente/" + paciente.getId() + "/atendimentos");
		Usuario usuario = (Usuario) auth.getPrincipal();

		try {
			atendimentoService.salvar(atendimento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_ATENDIMENTO);

			registroService.salvar(usuario.getId(), paciente.getId(), atendimento.getId(),
					Acao.CADASTRAR_NOVO_ATENDIMENTO, null);

		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@GetMapping("/editar/{idAtendimento}")
	public ModelAndView formularioEditarAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
			Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_ATENDIMENTO);
		Aluno aluno = (Aluno) auth.getPrincipal();
		modelAndView.addObject("atendimento", atendimento);
		modelAndView.addObject("turmas", turmaService.buscarAtivasPorAluno(aluno));
		modelAndView.addObject("paciente", atendimento.getPaciente());
		modelAndView.addObject("professor", atendimento.getProfessor());
		modelAndView.addObject("professores", atendimento.getTurma().getProfessores());
		modelAndView.addObject("auxiliar", atendimento.getAjudante());
		modelAndView.addObject("ajudantes", alunoService.buscarAjudantes(atendimento.getTurma().getId(), aluno));
		modelAndView.addObject("action", "editar");
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/editar")
	public ModelAndView editarAtendimento(Atendimento atendimento, RedirectAttributes attributes, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView("redirect:/atendimento/" + atendimento.getId());
		Usuario usuario = (Usuario) auth.getPrincipal();
		Atendimento atendimentoAnterior = atendimentoService.buscarPorId(atendimento.getId());

		try {
			atendimentoService.atualizar(atendimento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EDITAR_ATENDIMENTO);
			registroService.salvar(usuario.getId(), atendimento.getPaciente().getId(), atendimento.getId(),
					Acao.EDITAR_DADOS_ATENDIMENTO, atendimentoAnterior.toStringRegistro());
		} catch (ProntuarioException e) {
			modelAndView.addObject("atendimento", atendimento);
			modelAndView.addObject("paciente", atendimento.getPaciente());
			modelAndView.addObject("action", "editar");
			modelAndView.setViewName(FORMULARIO_ADICIONAR_ATENDIMENTO);
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE_VERIFICACAO_ESTUDANTE)
	@GetMapping("/excluir/{id}")
	public ModelAndView removerAtendimento(@PathVariable("id") @Param("atendimento") Atendimento atendimento,
			Authentication auth, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(
				"redirect:/paciente/" + atendimento.getPaciente().getId() + "/atendimentos");
		Usuario usuario = (Usuario) auth.getPrincipal();
		if (!atendimento.getStatus().equals(StatusAtendimento.EM_ANDAMENTO)) {
			attributes.addFlashAttribute(ERROR, ERRO_EXCLUIR_ATENDIMENTO);
		} else {

			registroService.salvar(usuario.getId(), atendimento.getPaciente().getId(), atendimento.getId(),
					Acao.REMOVER_ATENDIMENTO, atendimento.toStringRegistro());
			atendimentoService.remover(atendimento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_ATENDIMENTO);
		}
		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR)
	@PostMapping("/avaliar/{idAtendimento}")
	public ModelAndView avaliarAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
			AvaliacaoAtendimento avaliacaoAtendimento, Authentication auth, RedirectAttributes attributes) {
		atendimento.getAvaliacao().setObservacao(avaliacaoAtendimento.getObservacao());
		atendimento.getAvaliacao().setItens(avaliacaoAtendimento.getItens());
		Usuario usuario = (Usuario) auth.getPrincipal();
		try {
			atendimentoService.atualizar(atendimento);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + atendimento.getId());
		attributes.addFlashAttribute(SUCCESS, SUCCESS_AVALIAR_ATENDIMENTO);
		registroService.salvar(usuario.getId(), atendimento.getPaciente().getId(), atendimento.getId(),
				Acao.AVALIAR_ATENDIMENTO, atendimento.toStringRegistro());
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE_VERIFICACAO_ESTUDANTE)
	@GetMapping({ "/finalizar/{id}", "/{id}/finalizar/odontograma" })
	public ModelAndView finalizarAtendimento(@PathVariable("id") Atendimento atendimento, RedirectAttributes attributes,
			HttpServletRequest request, Authentication auth) {
		ModelAndView modelAndView = null;
		atendimentoService.finalizarAtendimento(atendimento);
		Usuario usuario = (Usuario) auth.getPrincipal();
		if (request.getRequestURI().contains("odontograma")) {
			modelAndView = new ModelAndView(REDIRECT_ODONTOGRAMA + atendimento.getPaciente().getId());
		} else {
			modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + atendimento.getId());
			modelAndView.addObject("atendimento", atendimento);
		}
		attributes.addFlashAttribute(SUCCESS, SUCCESS_FINALIZAR_ATENDIMENTO);
		registroService.salvar(usuario.getId(), atendimento.getPaciente().getId(), atendimento.getId(),
				Acao.FINALIZAR_ATENDIMENTO, (atendimento != null ? atendimento.toStringRegistro() : "Null"));
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_PROFESSOR_VERIFICACAO_PROFESSOR)
	@GetMapping("/validar/{id}")
	public ModelAndView validarAtendimento(@PathVariable("id") Atendimento atendimento, RedirectAttributes attributes,
			Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + atendimento.getId());
		Usuario usuario = (Usuario) auth.getPrincipal();
		atendimentoService.validarAtendimento(atendimento);
		attributes.addFlashAttribute(SUCCESS, SUCCESS_VALIDAR_ATENDIMENTO);
		registroService.salvar(usuario.getId(), atendimento.getPaciente().getId(), atendimento.getId(),
				Acao.VALIDAR_ATENDIMENTO, atendimento.toStringRegistro());
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO)
	@GetMapping("/{idAtendimento}/excluir-procedimento/{idProcedimento}")
	public ModelAndView removerProcedimento(@PathVariable("idProcedimento") Procedimento procedimento,
			@PathVariable("idAtendimento") @Param("atendimento") Atendimento atendimento, Authentication auth,
			RedirectAttributes attributes) {
		Usuario usuario = (Usuario) auth.getPrincipal();
		ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + atendimento.getId());
		registroService.salvar(usuario.getId(), atendimento.getPaciente().getId(), procedimento.getId(),
				Acao.REMOVER_PROCEDIMENTO, procedimento.toStringRegistro());
		procedimentoService.excluir(procedimento.getId());
		attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_PROCEDIMENTO);

		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO)
	@GetMapping("/{idAtendimento}/excluir-patologia/{idPatologia}")
	public ModelAndView removerPatologia(@PathVariable("idPatologia") Patologia patologia,
			@PathVariable("idAtendimento") @Param("atendimento") Atendimento atendimento, Authentication auth,
			RedirectAttributes attributes) {
		Usuario usuario = (Usuario) auth.getPrincipal();
		ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + atendimento.getId());
		registroService.salvar(usuario.getId(), atendimento.getPaciente().getId(), patologia.getId(),
				Acao.REMOVER_PATOLOGIA, patologia.toStringRegistro());
		patologiaService.excluir(patologia.getId());
		attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_PATOLOGIA);

		return modelAndView;
	}
}
