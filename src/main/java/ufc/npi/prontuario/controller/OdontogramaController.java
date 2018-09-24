package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_ODONTOGRAMA;
import static ufc.npi.prontuario.util.FragmentsConstants.FRAGMENT_MODAL_DETALHES_PATOLOGIA;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_ODONTOGRAMA;
import static ufc.npi.prontuario.util.PagesConstants.TABLE_PATOLOGIAS;
import static ufc.npi.prontuario.util.PagesConstants.TABLE_PROCEDIMENTOS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Estrutura;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.model.Registro.Acao;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.model.TipoProcedimento;
import ufc.npi.prontuario.model.Tratamento;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.model.enums.Papel;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.EstruturaService;
import ufc.npi.prontuario.service.OdontogramaService;
import ufc.npi.prontuario.service.PatologiaService;
import ufc.npi.prontuario.service.ProcedimentoService;
import ufc.npi.prontuario.service.RegistroService;
import ufc.npi.prontuario.service.TipoPatologiaService;
import ufc.npi.prontuario.service.TipoProcedimentoService;
import ufc.npi.prontuario.view.ProntuarioView;

//TODO Refatorar os métodos para atender a nova modelagem.
@Controller
@RequestMapping("/odontograma")
public class OdontogramaController {

	@Autowired
	private AlunoService alunoService;

	@Autowired
	private OdontogramaService odontogramaService;

	@Autowired
	private PatologiaService patologiaService;

	@Autowired
	private ProcedimentoService procedimentoService;

	@Autowired
	private TipoPatologiaService tipoPatologiaService;

	@Autowired
	private TipoProcedimentoService tipoProcedimentoService;

	@Autowired
	private AtendimentoService atendimentoService;

	@Autowired
	private RegistroService registroService;

	@Autowired
	private EstruturaService estruturaService;

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/paciente/{id}")
	public ModelAndView novoOdontogramaPatologias(@PathVariable("id") Paciente paciente, Authentication auth) {
		ModelAndView mv = new ModelAndView(PAGINA_ODONTOGRAMA);
		mv.addObject("patologias", tipoPatologiaService.buscarTudo());
		mv.addObject("procedimentos", tipoProcedimentoService.buscarTudo());
		mv.addObject("paciente", paciente);

		Collection<? extends GrantedAuthority> papeis = auth.getAuthorities();

		if (papeis.contains(Papel.ESTUDANTE)) {
			Aluno aluno = (Aluno) auth.getPrincipal();
			Atendimento atendimento = atendimentoService.buscarUltimoAtendimentoAbertoPorAlunoEPaciente(aluno,
					paciente);
			Odontograma odontograma = null;
			if (atendimento != null) {
				odontograma = odontogramaService.buscarPorAtendimento(atendimento);
			}
			mv.addObject("existeAtendimento", atendimentoService.existeAtendimentoAbertoAlunoPaciente(aluno, paciente));
			mv.addObject("atendimento", atendimento);
			mv.addObject("odontograma", odontograma);
		} else if (papeis.contains(Papel.PROFESSOR) || papeis.contains(Papel.ADMINISTRACAO)) {
			mv.addObject("existeAtendimento", false);
		}

		return mv;
	}

	@GetMapping("/tablePatologias/{id}")
	public ModelAndView tablePatologias(@PathVariable("id") Paciente paciente, Authentication auth) {
		ModelAndView mv = new ModelAndView(TABLE_PATOLOGIAS);
		Usuario usuario = (Usuario) auth.getPrincipal();
		mv.addObject("patologias", patologiaService.buscarPatologiasDoPacientePorUsuario(paciente, usuario));
		if (usuario instanceof Aluno)
			mv.addObject("existeAtendimento",
					atendimentoService.existeAtendimentoAbertoAlunoPaciente((Aluno) usuario, paciente));
		return mv;
	}

	@GetMapping("/tableProcedimentos/{id}")
	public ModelAndView tableProcedimentos(@PathVariable("id") Paciente paciente, Authentication auth) {
		ModelAndView mv = new ModelAndView(TABLE_PROCEDIMENTOS);
		Usuario usuario = (Usuario) auth.getPrincipal();
		List<Procedimento> procedimentos = procedimentoService.buscarProcedimentosPaciente(paciente, usuario);
		// procedimentos.addAll(procedimentoService.buscarProcedimentosExistentesOdontograma(odontograma,
		// usuario.getId()));
		// procedimentos.sort((p1, p2) ->
		// p1.getAtendimento().getData().compareTo(p1.getAtendimento().getData()));
		mv.addObject("procedimentos", procedimentos);
		return mv;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/adicionarPatologia")
	public ResponseEntity<String> adicionarPatologia(@ModelAttribute("idEstrutura") Integer idEstrutura,
			@ModelAttribute("patologias") List<Integer> patologiasId,
			@ModelAttribute("idOdontograma") Integer idOdontograma, @ModelAttribute("descricao") String descricao,
			Authentication auth) throws ProntuarioException {
		try {
			Usuario usuario = (Usuario) auth.getPrincipal();
			Odontograma odontograma = odontogramaService.buscarPorId(idOdontograma);
			Aluno aluno = alunoService.buscarPorMatricula(usuario.getMatricula());

			Estrutura estrutura = estruturaService.buscarPorId(idEstrutura);

			patologiaService.salvar(estrutura, patologiasId, odontograma, descricao, aluno);
			registroService.salvar(usuario.getId(), odontograma.getAtendimento().getPaciente().getId(), null,
					Acao.CADASTRAR_PATOLOGIA, descricao);
			return new ResponseEntity<String>("Cadastrado com sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Erro ao cadastrar", HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@GetMapping("/adicionarOdontograma/{id}")
	public ModelAndView adicionarOdontograma(@PathVariable("id") Paciente paciente, RedirectAttributes attributes,
			Authentication auth) throws ProntuarioException {
		ModelAndView modelAndView = new ModelAndView("redirect:/odontograma/paciente/" + paciente.getId());

		Usuario usuario = (Usuario) auth.getPrincipal();
		Aluno aluno = alunoService.buscarPorMatricula(usuario.getMatricula());

		if (atendimentoService.existeAtendimentoAbertoAlunoPaciente(aluno, paciente)) {
			Atendimento atendimento = atendimentoService.buscarUltimoAtendimentoAbertoPorAlunoEPaciente(aluno,
					paciente);
			Odontograma odontograma = odontogramaService.buscarPorAtendimento(atendimento);
			if (odontograma == null) {
				atendimentoService.adicionarOdontograma(atendimento);
			} else {
				attributes.addFlashAttribute(ERROR, ERRO_ADICIONAR_ODONTOGRAMA);
			}
		}

		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/adicionarProcedimento")
	public ResponseEntity<String> adicionarProcedimento(@ModelAttribute("atendimento") Atendimento atendimento,
			@ModelAttribute("idEstrutura") Integer idEstrutura,
			@ModelAttribute("procedimentos") List<Integer> procedimentosId,
			@RequestParam(value = "patologias", required = false) List<Integer> patologias,
			@ModelAttribute("idOdontograma") Integer idOdontograma, @RequestParam("descricao") String descricao,
			@RequestParam("preExistente") Boolean preExistente,
			@ModelAttribute("data") @DateTimeFormat(pattern = DATE_PATTERN) Date data, Authentication auth)
			throws ProntuarioException {
		Usuario usuario = (Usuario) auth.getPrincipal();
		Aluno aluno = alunoService.buscarPorMatricula(usuario.getMatricula());
		Odontograma odontograma = odontogramaService.buscarPorId(idOdontograma);

		Estrutura estrutura = estruturaService.buscarPorId(idEstrutura);

		procedimentoService.salvar(estrutura, procedimentosId, odontograma, descricao, aluno, preExistente, patologias,
				data);

		registroService.salvar(usuario.getId(), odontograma.getAtendimento().getPaciente().getId(), null,
				Acao.CADASTAR_PROCEDIMENTO, null);

		return new ResponseEntity<String>("Cadastrado com sucesso", HttpStatus.OK);
	}

	@GetMapping("/patologia/paciente/{pacienteId}")
	@JsonView(ProntuarioView.OdontogramaView.class)
	public @ResponseBody List<Patologia> buscarPatologiasPaciente(@PathVariable("pacienteId") Paciente paciente,
			Authentication auth) {
		Usuario usuario = (Usuario) auth.getPrincipal();
		List<Patologia> patologias = patologiaService.buscarPatologiasDoPacientePorUsuario(paciente, usuario);
		return patologias;
	}

	@GetMapping("/procedimentos/paciente/{pacienteId}")
	@JsonView(ProntuarioView.OdontogramaView.class)
	public @ResponseBody List<Procedimento> buscarProcedimentosPaciente(@PathVariable("pacienteId") Paciente paciente,
			@RequestParam(value = "preExistente", required = false) Boolean preExistente, Authentication auth) {
		Usuario usuario = (Usuario) auth.getPrincipal();
		if (preExistente == null)
			preExistente = false;
		List<Procedimento> procedimentos = procedimentoService.buscarProcedimentosPacienteUsuarioPre(paciente, usuario,
				preExistente);
		return procedimentos;
	}

	@GetMapping("/buscarProcedimentosAtendimento/{atendimento}")
	public @ResponseBody Map<String, Object> buscarProcedimentosAtendimento(
			@PathVariable("atendimento") Atendimento atendimento, Authentication auth) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("procedimentosOdontograma", procedimentoService.buscarPorAtendimento(atendimento));
		return map;
	}

	@GetMapping("/patologias/{idPatologia}")
	@JsonView(ProntuarioView.OdontogramaView.class)
	public ModelAndView buscarPatologia(@PathVariable("idPatologia") Patologia patologia) {
		ModelAndView modelAndView = new ModelAndView(FRAGMENT_MODAL_DETALHES_PATOLOGIA);
		modelAndView.addObject("patologia", patologia);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/tratar/{idPatologia}")
	public ResponseEntity<?> tratarPatologia(@PathVariable("idPatologia") Patologia patologia,
			@ModelAttribute Tratamento tratamento, Authentication auth) {
		Aluno aluno = (Aluno) auth.getPrincipal();
		tratamento.setResponsavel(aluno);

		patologiaService.tratar(patologia, tratamento);
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Tratado com sucesso", HttpStatus.OK);
		return responseEntity;
	}

	@GetMapping("/buscar-patologia")
	public @ResponseBody List<TipoPatologia> buscaPatologia(@RequestParam("query") String query) {

		List<TipoPatologia> resultado = tipoPatologiaService.buscarPorNome(query);

		return resultado;
	}

	@GetMapping("/buscar-procedimento")
	public @ResponseBody List<TipoProcedimento> buscaProcedimento(@RequestParam("query") String query) {

		List<TipoProcedimento> resultado = tipoProcedimentoService.buscarPorNome(query);

		return resultado;
	}

	@GetMapping("/buscar-patologia/{estruturaId}")
	@JsonView(ProntuarioView.OdontogramaView.class)
	public @ResponseBody List<Patologia> buscaTodasPatologias(@PathVariable("estruturaId") Estrutura estrutura,
			Authentication auth) {

		Aluno aluno = (Aluno) auth.getPrincipal();
		List<Patologia> patologias = patologiaService
				.buscarPatologiasPorEstruturaAlunoEAtendimentoEmAndamento(estrutura, aluno);

		return patologias;
	}

	@GetMapping("/buscar-procedimentos/{estruturaId}")
	@JsonView(ProntuarioView.OdontogramaView.class)
	public @ResponseBody List<Procedimento> buscaProcedimentosPorEstrutura(
			@PathVariable("estruturaId") Estrutura estrutura,
			@RequestParam(value = "preExistente", required = false) Boolean preExistente, Authentication auth) {
		try {
			Aluno aluno = (Aluno) auth.getPrincipal();
			if (preExistente == null)
				preExistente = false;
			List<Procedimento> procedimentos = procedimentoService.buscarProcedimentosEstruturaAndamento(estrutura,
					aluno, preExistente);
			return procedimentos;
		} catch (Exception e) {
			return new ArrayList<Procedimento>();
		}
	}

	@GetMapping("/patologia/delete/{id}")
	public ResponseEntity<String> deletePatologiaById(@PathVariable("id") int id) {
		try {
			patologiaService.excluir(id);
			return new ResponseEntity<String>("Apagado com sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Erro ao apagar", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/patologia/delete-all/{estruturaId}")
	public ResponseEntity<String> deletePatologiasByEstrutura(@PathVariable("estruturaId") Estrutura estrutura,
			Authentication auth) {

		try {
			Aluno aluno = (Aluno) auth.getPrincipal();
			patologiaService.excluirTudoPorEstruturaEAluno(estrutura, aluno);
			return new ResponseEntity<String>("Apagado com sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Erro ao apagar", HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/patologia/edit/{id}")
	public ResponseEntity<?> editarPatologia(@PathVariable("id") int id,
			@RequestParam(value = "descricao", required = false) String descricao, Authentication auth) {
		Patologia patologia = patologiaService.buscarPorId(id);
		patologia.setDescricao(descricao);
		patologiaService.atualizar(patologia);
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Atualizado com sucesso", HttpStatus.OK);
		return responseEntity;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/procedimento/edit/{id}")
	public ResponseEntity<?> editarProcedimento(@PathVariable("id") int id,
			@RequestParam(value = "descricao", required = false) String descricao, Authentication auth) {
		Procedimento procedimento = procedimentoService.buscarPorId(id);
		procedimento.setDescricao(descricao);
		procedimentoService.atualizar(procedimento);
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Atualizado com sucesso", HttpStatus.OK);
		return responseEntity;
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@GetMapping("/procedimento/delete/{id}")
	public ResponseEntity<String> deleteProcedimentoById(@PathVariable("id") int id) {
		try {
			procedimentoService.excluir(id);
			return new ResponseEntity<String>("Apagado com sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Erro ao apagar", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/procedimento/delete-all/{estruturaId}")
	public ResponseEntity<String> deleteProcedimentosByEstrutura(@PathVariable("estruturaId") Estrutura estrutura,
			@RequestParam(value = "preExistente", required = false) Boolean preExistente, Authentication auth) {

		try {
			Aluno aluno = (Aluno) auth.getPrincipal();
			if (preExistente == null)
				preExistente = false;
			procedimentoService.excluirTodosPorEstruturaEAlunoPre(estrutura, aluno, preExistente);
			return new ResponseEntity<String>("Apagado com sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Erro ao apagar", HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@GetMapping("/pre-procedimento/delete/{id}")
	public ResponseEntity<String> deleteProcedimentoByIdPre(@PathVariable("id") int id) {
		try {
			procedimentoService.excluir(id);
			return new ResponseEntity<String>("Apagado com sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Erro ao apagar", HttpStatus.BAD_REQUEST);
		}
	}

}