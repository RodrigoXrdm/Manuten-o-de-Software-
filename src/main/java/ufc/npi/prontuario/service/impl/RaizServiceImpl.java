package ufc.npi.prontuario.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Raiz;
import ufc.npi.prontuario.model.enums.NomeRaiz;
import ufc.npi.prontuario.model.enums.NumeroDente;
import ufc.npi.prontuario.repository.RaizRepository;
import ufc.npi.prontuario.service.RaizService;

@Service
public class RaizServiceImpl implements RaizService {

	@Autowired
	private RaizRepository raizRepository;

	@Override
	public List<Raiz> getByDente(Dente dente) {
		return raizRepository.getByDente(dente);
	}

	@Override
	public List<Raiz> criarRaizes(Paciente paciente, Dente dente){
		List<Raiz> raizes = new ArrayList<>();
		
		Raiz raiz1 = new Raiz();
		raiz1.setPaciente(paciente);
		raiz1.setDente(dente);
		raiz1.setPresente(true);
		raiz1.setNome(NomeRaiz.R1);
		
		raizes.add(raiz1);
		
		// Possuem mais de 1 raíz: 	18, 17, 16, -, 14/ - - - - - -/ 24, - , 26, 27, 28
		//							48, 47, 46, -, -/ - - - - - -/ -, - , 36, 37, 38
		if(dente.getNumero().equals(NumeroDente.D18) ||
				dente.getNumero().equals(NumeroDente.D17) ||
				dente.getNumero().equals(NumeroDente.D16) ||
				dente.getNumero().equals(NumeroDente.D14) ||
				dente.getNumero().equals(NumeroDente.D24) ||
				dente.getNumero().equals(NumeroDente.D26) ||
				dente.getNumero().equals(NumeroDente.D27) ||
				dente.getNumero().equals(NumeroDente.D28) || 
				dente.getNumero().equals(NumeroDente.D48) || 
				dente.getNumero().equals(NumeroDente.D47) || 
				dente.getNumero().equals(NumeroDente.D46) || 
				dente.getNumero().equals(NumeroDente.D36) || 
				dente.getNumero().equals(NumeroDente.D37) || 
				dente.getNumero().equals(NumeroDente.D38)){
			
			Raiz raiz2 = new Raiz();
			raiz2.setPaciente(paciente);
			raiz2.setDente(dente);
			raiz2.setPresente(true);
			raiz2.setNome(NomeRaiz.R2);
			
			raizes.add(raiz2);
			
			// Possuem 3 raízes: 	18, 17, 16, -, -/ -, -, -, -, -, -/ -, - 26, 27, 28
			if(dente.getNumero().equals(NumeroDente.D18) ||
					dente.getNumero().equals(NumeroDente.D17) ||
					dente.getNumero().equals(NumeroDente.D16) ||
					dente.getNumero().equals(NumeroDente.D26) ||
					dente.getNumero().equals(NumeroDente.D27) ||
					dente.getNumero().equals(NumeroDente.D28)){
				Raiz raiz3 = new Raiz();
				raiz3.setPaciente(paciente);
				raiz3.setDente(dente);
				raiz3.setPresente(true);
				raiz3.setNome(NomeRaiz.R3);
				
				raizes.add(raiz3);
			}
		}
		
		return raizes;
	}

}
