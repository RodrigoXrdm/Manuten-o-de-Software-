package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_PACIENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_PACIENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_REALIZAR_ANAMNESE;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_CADASTRO_PACIENTE;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_REALIZAR_ANAMNESE;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_ANAMNESE_PACIENTE;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_PACIENTE;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_ANAMNESES_PACIENTE;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_ATENDIMENTOS;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_PACIENTES;
import static ufc.npi.prontuario.util.PagesConstants.TABLE_ATENDIMENTOS;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_CADASTRAR_PACIENTE;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_PACIENTES;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PacienteAnamnese;
import ufc.npi.prontuario.model.Registro.Acao;
import ufc.npi.prontuario.model.Usuario;
//import ufc.npi.prontuario.model.Registro.*;
import ufc.npi.prontuario.model.enums.Estado;
import ufc.npi.prontuario.model.enums.EstadoCivil;
import ufc.npi.prontuario.model.enums.Raca;
import ufc.npi.prontuario.model.enums.Sexo;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.AnamneseService;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.DenteService;
import ufc.npi.prontuario.service.PacienteService;
import ufc.npi.prontuario.service.RegistroService;
import ufc.npi.prontuario.view.ProntuarioView;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private AnamneseService anamneseService;

	@Autowired
	private AlunoService alunoService;

	@Autowired
	private AtendimentoService atendimentoService;

	@Autowired
	private RegistroService registroService;

	@Autowired
	private DenteService denteService;

	@GetMapping
	public ModelAndView listagemPacientes() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_PACIENTES);

		modelAndView.addObject("pacientes", pacienteService.buscarTudo());

		return modelAndView;
	}

	@GetMapping("/cadastrar")
	public ModelAndView paginaCadastroPaciente(Paciente paciente) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_CADASTRO_PACIENTE);

		modelAndView.addObject("sexo", Sexo.values());
		modelAndView.addObject("estado", Estado.values());
		modelAndView.addObject("estadoCivil", EstadoCivil.values());
		modelAndView.addObject("raca", Raca.values());
		modelAndView.addObject("action", "cadastrar");

		return modelAndView;
	}

	@PostMapping("/cadastrar")
	public ModelAndView cadastrarPaciente(@RequestParam(value = "button", required = false) String button,
			Paciente paciente, RedirectAttributes attributes, Authentication auth) {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_CADASTRAR_PACIENTE);
		Usuario usuario = (Usuario) auth.getPrincipal();

		try {
			pacienteService.salvar(paciente);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_PACIENTE);
			registroService.salvar(usuario.getId(), paciente.getId(), paciente.getId(), Acao.CADASTRAR_NOVO_PACIENTE,
					null);
			if (button.equals("salvar")) {
				modelAndView.setViewName("redirect:/paciente/" + paciente.getId());
			} else {
				modelAndView.setViewName(REDIRECT_CADASTRAR_PACIENTE);
			}

		} catch (ProntuarioException e) {
			modelAndView.addObject("sexo", Sexo.values());
			modelAndView.addObject("estado", Estado.values());
			modelAndView.addObject("estadoCivil", EstadoCivil.values());
			modelAndView.addObject("raca", Raca.values());
			modelAndView.addObject("action", "cadastrar");
			modelAndView.addObject(ERROR, e.getMessage());
			modelAndView.setViewName(FORMULARIO_CADASTRO_PACIENTE);
		}

		return modelAndView;
	}

	@GetMapping("/editar/{idPaciente}")
	public ModelAndView editarPaciente(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_CADASTRO_PACIENTE);

		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("sexo", Sexo.values());
		modelAndView.addObject("estado", Estado.values());
		modelAndView.addObject("estadoCivil", EstadoCivil.values());
		modelAndView.addObject("action", "editar");

		return modelAndView;
	}

	@PostMapping("/editar")
	public ModelAndView editarPaciente(Paciente paciente, RedirectAttributes attributes, Authentication auth) {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_PACIENTES);
		Usuario usuario = (Usuario) auth.getPrincipal();
		Paciente pacienteAnterior = pacienteService.buscarPorId(paciente.getId());
		String dadosPaciente = pacienteAnterior.toStringRegistro();
		try {
			pacienteService.salvar(paciente);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EDITAR_PACIENTE);
			registroService.salvar(usuario.getId(), paciente.getId(), paciente.getId(), Acao.EDITAR_DADOS_PACIENTE,
					dadosPaciente);
		} catch (ProntuarioException e) {
			modelAndView.addObject("sexo", Sexo.values());
			modelAndView.addObject("estado", Estado.values());
			modelAndView.addObject("estadoCivil", EstadoCivil.values());
			modelAndView.addObject("action", "editar");
			modelAndView.addObject(ERROR, e.getMessage());
			modelAndView.setViewName(FORMULARIO_CADASTRO_PACIENTE);
		}

		return modelAndView;
	}

	@GetMapping("/{idPaciente}")
	public ModelAndView visualizarDetalhes(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_PACIENTE);
		modelAndView.addObject("paciente", paciente);
		return modelAndView;
	}

	@GetMapping("/{idPaciente}/dentes")
	@JsonView(ProntuarioView.OdontogramaView.class)
	public @ResponseBody List<Dente> dentesPaciente(@PathVariable("idPaciente") Paciente paciente) {
		List<Dente> dentes = denteService.getByPaciente(paciente);
		return dentes;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idPaciente}/anamneses")
	public ModelAndView listarAnamneses(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_ANAMNESES_PACIENTE);
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("anamneses", anamneseService.buscarTodasFinalizadas());
		modelAndView.addObject("pacienteAnamneses", paciente.getPacienteAnamneses());
		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idPaciente}/atendimentos")
	public ModelAndView listarAtendimentos(@PathVariable("idPaciente") Paciente paciente, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_ATENDIMENTOS);
		Usuario usuario = (Usuario) auth.getPrincipal();
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("atendimentos",
				atendimentoService.buscarAtendimentosPorUsuario(usuario.getId(), paciente));
		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idPaciente}/tableAtendimentos")
	public ModelAndView getTableAtendimentos(@PathVariable("idPaciente") Paciente paciente, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(TABLE_ATENDIMENTOS);
		Usuario usuario = (Usuario) auth.getPrincipal();
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("atendimentos",
				atendimentoService.buscarAtendimentosPorUsuario(usuario.getId(), paciente));
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@GetMapping("/{idPaciente}/anamnese")
	public ModelAndView realizarAnamneseForm(@PathVariable("idPaciente") Paciente paciente,
			@RequestParam("idAnamnese") Anamnese anamnese, PacienteAnamnese pacienteAnamnese) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_REALIZAR_ANAMNESE);

		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("anamnese", anamnese);
		modelAndView.addObject("pacienteAnamnese", pacienteAnamnese);

		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/{idPaciente}/anamnese")
	public ModelAndView realizarAnamnese(@PathVariable("idPaciente") Paciente paciente,
			PacienteAnamnese pacienteAnamnese, Authentication auth, RedirectAttributes attributes) {

		Usuario usuario = (Usuario) auth.getPrincipal();

		pacienteAnamnese.setResponsavel(alunoService.buscarPorMatricula(usuario.getMatricula()));
		pacienteService.adicionarAnamnese(paciente, pacienteAnamnese);

		attributes.addFlashAttribute(SUCCESS, SUCCESS_REALIZAR_ANAMNESE);

		registroService.salvar(usuario.getId(), paciente.getId(), pacienteAnamnese.getAnamnese().getId(),
				Acao.CADASTRAR_ANAMNESE, null);

		return new ModelAndView("redirect:/paciente/" + paciente.getId() + "/anamneses");
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idPaciente}/anamnese/{idAnamnese}")
	public ModelAndView visualizarDetalhesAnamnese(@PathVariable("idPaciente") Paciente paciente,
			@PathVariable("idAnamnese") PacienteAnamnese anamnese) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_ANAMNESE_PACIENTE);
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("anamnese", anamnese);
		return modelAndView;
	}
}
