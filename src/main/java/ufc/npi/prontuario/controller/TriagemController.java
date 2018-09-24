package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.PagesConstants.PAGINA_TRIAGEM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.enums.StatusPlanoTratamento;
import ufc.npi.prontuario.service.DisciplinaService;
import ufc.npi.prontuario.service.PlanoTratamentoService;

@Controller
@RequestMapping("/triagem")
public class TriagemController {

	@Autowired
	private DisciplinaService disciplinaService;

	@Autowired
	private PlanoTratamentoService tratamentoService;

	@GetMapping
	public ModelAndView triagemForm() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_TRIAGEM);
		modelAndView.addObject("clinicas", disciplinaService.buscarTudo());
		modelAndView.addObject("status", StatusPlanoTratamento.values());
		modelAndView.addObject("busca", false);
		return modelAndView;
	}

	@PostMapping
	public ModelAndView triagem(@RequestParam("clinica") Disciplina disciplina,
			@RequestParam(value = "status", required = false) String status) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_TRIAGEM);
		modelAndView.addObject("tratamentos",
				tratamentoService.buscarPlanoTratamentoPorClinicaEStatus(disciplina, status));
		modelAndView.addObject("clinicas", disciplinaService.buscarTudo());
		modelAndView.addObject("status", StatusPlanoTratamento.values());
		modelAndView.addObject("clinicaSelecionada", disciplina != null ? disciplina.getNome() : "-");
		modelAndView.addObject("statusSelecionado", status.isEmpty() ? "-" : status);
		modelAndView.addObject("busca", true);
		return modelAndView;
	}

}
