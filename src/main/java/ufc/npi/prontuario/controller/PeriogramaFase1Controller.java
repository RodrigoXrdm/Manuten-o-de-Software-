package ufc.npi.prontuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.Sextante;
import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.NumeroDente;
import ufc.npi.prontuario.model.enums.StatusPeriograma;
import ufc.npi.prontuario.service.PeriogramaFase1Service;
import ufc.npi.prontuario.view.PeriogramaDTO;
import ufc.npi.prontuario.view.ProntuarioView;

@RestController
@RequestMapping("periograma-fase1")
public class PeriogramaFase1Controller {

	@Autowired
	private PeriogramaFase1Service periogramaFase1Service;
	
	@GetMapping("/{periogramaId}")
	@JsonView(ProntuarioView.PeriogramaView.class)
	public ResponseEntity<PeriogramaDTO> index(
			@PathVariable("periogramaId") Periograma periograma) {
		PeriogramaDTO periogramaDTO = periogramaFase1Service.carregarPeriograma(periograma);
		return new ResponseEntity<>(periogramaDTO, HttpStatus.OK);
	}
	
	@PutMapping("/status")
	public ResponseEntity<StatusPeriograma> atualizarStatus(@RequestParam("periogramaId") Periograma periograma,
			@RequestParam("status") StatusPeriograma statusPeriograma) {
		return new ResponseEntity<>(periogramaFase1Service.atualizarStatusPeriograma(periograma, statusPeriograma), HttpStatus.OK);
	}
	
	@PostMapping("/psr")
	@JsonView(ProntuarioView.PeriogramaView.class)
	public ResponseEntity<Patologia> registrarPsr(@RequestParam("periogramaId") Periograma periograma,  
			@RequestParam("sextanteId") Sextante sextante, 
			@RequestParam("descricao") String descricao) {
		
		try {
			return new ResponseEntity<>(periogramaFase1Service.salvarPsr(periograma, sextante, descricao), HttpStatus.CREATED);
		} catch (ProntuarioException e) {
			e.printStackTrace();
			return new ResponseEntity<> (null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/calculo-supragengival")
	@JsonView(ProntuarioView.PeriogramaView.class)
	public ResponseEntity<Patologia> registrarCalculoSupragengival(@RequestParam("periogramaId") Periograma periograma,
			@RequestParam("pacienteId") Paciente paciente,
			@RequestParam("numeroDente") NumeroDente numeroDente){
		try {
			return new ResponseEntity<>(periogramaFase1Service.salvarCalculoSupragengival(periograma, paciente, numeroDente), HttpStatus.ACCEPTED);
		} catch (ProntuarioException e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@DeleteMapping("/calculo-supragengival")
	public ResponseEntity<Boolean> removerCalculoSupragengival(@RequestParam("periogramaId") Periograma periograma,
			@RequestParam("pacienteId") Paciente paciente,
			@RequestParam("numeroDente") NumeroDente numeroDente){
		periogramaFase1Service.removerCalculoSupragengival(periograma, paciente, numeroDente);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
	
	@PostMapping("/outros-fatores")
	@JsonView(ProntuarioView.PeriogramaView.class)
	public ResponseEntity<Patologia> registrarOutrosFatores(@RequestParam("periogramaId") Periograma periograma,
			@RequestParam("pacienteId") Paciente paciente,
			@RequestParam("numeroDente") NumeroDente numeroDente,
			@RequestParam("descricao") String descricao){
		
		try {
			return new ResponseEntity<>(periogramaFase1Service.salvarOutrosFatores(periograma, paciente, numeroDente, descricao), HttpStatus.OK);
		} catch (ProntuarioException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@PostMapping("/sangramento-marginal")
	@JsonView(ProntuarioView.PeriogramaView.class)
	public ResponseEntity<Patologia> registrarSangramentoMarginal (@RequestParam("periogramaId") Periograma periograma,
			@RequestParam("pacienteId") Paciente paciente,
			@RequestParam("numeroDente") NumeroDente numeroDente,
			@RequestParam("face") NomeFace nomeFace){
		
		try {
			return new ResponseEntity<>(periogramaFase1Service.salvarSangramentoMarginal(periograma, paciente, numeroDente, nomeFace), HttpStatus.OK);
					
		} catch (ProntuarioException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@DeleteMapping("/sangramento-marginal")
	public ResponseEntity<Boolean> removerSangramentoMarginal (@RequestParam("periogramaId") Periograma periograma,
			@RequestParam("pacienteId") Paciente paciente,
			@RequestParam("numeroDente") NumeroDente numeroDente,
			@RequestParam("face") NomeFace nomeFace){
		periogramaFase1Service.removerSangramentoMarginal(periograma, paciente, numeroDente, nomeFace);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
	
	@PostMapping("/placa-bacteriana")
	@JsonView(ProntuarioView.PeriogramaView.class)
	public ResponseEntity<Patologia> registrarPlacaBacteriana (@RequestParam("periogramaId") Periograma periograma,
			@RequestParam("pacienteId") Paciente paciente,
			@RequestParam("numeroDente") NumeroDente numeroDente,
			@RequestParam("face") NomeFace nomeFace){
		
		try {
			return new ResponseEntity<>(periogramaFase1Service.salvarPlacaBacteriana(periograma, paciente, numeroDente, nomeFace), HttpStatus.OK);
					
		} catch (ProntuarioException e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
		
	}
	
	@DeleteMapping("/placa-bacteriana")
	public ResponseEntity<Boolean> removerPlacaBacteriana (@RequestParam("periogramaId") Periograma periograma,
			@RequestParam("pacienteId") Paciente paciente,
			@RequestParam("numeroDente") NumeroDente numeroDente,
			@RequestParam("face") NomeFace nomeFace){
		periogramaFase1Service.removerPlacaBacteriana(periograma, paciente, numeroDente, nomeFace);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}

	
}
