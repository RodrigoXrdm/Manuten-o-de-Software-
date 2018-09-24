package ufc.npi.prontuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Raiz;
import ufc.npi.prontuario.service.RaizService;

@Controller
@RequestMapping("/dente")
public class DenteController {
	
	@Autowired
	private RaizService raizService;
	
	@GetMapping("/{id}/raizes")
	public @ResponseBody List<Raiz> buscaRaizesByDente(
			@PathVariable("id") Dente dente,
			Authentication auth) {
		return raizService.getByDente(dente);
	}
}
