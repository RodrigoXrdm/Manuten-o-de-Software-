package ufc.npi.prontuario.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Arcada;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Sextante;
import ufc.npi.prontuario.model.enums.NomeSextante;
import ufc.npi.prontuario.model.enums.TipoArcada;
import ufc.npi.prontuario.repository.SextanteRepository;
import ufc.npi.prontuario.service.DenteService;
import ufc.npi.prontuario.service.SextanteService;

@Service
public class SextanteServiceImpl implements SextanteService{
	
	@Autowired
	private DenteService denteService;
	
	@Autowired 
	SextanteRepository sextanteRepository;
	
	@Override
	public List<Sextante> findAllByPacienteOrderByNomeAsc(Paciente paciente) {
		return sextanteRepository.findAllByPacienteOrderByNomeAsc(paciente);
	}
	
	@Override
	public List<Sextante> criarSextantes(Paciente paciente, Arcada arcada){

		List<Sextante> sextantes = new ArrayList<>();
		
		if(arcada.getTipoArcada().equals(TipoArcada.SUPERIOR)){
			Sextante sextante1 = new Sextante();
			sextante1.setPaciente(paciente);
			sextante1.setArcada(arcada);
			sextante1.setNome(NomeSextante.S1);
			sextante1.setDentes(denteService.criarDentes(paciente, sextante1));
			
			Sextante sextante2 = new Sextante();
			sextante2.setPaciente(paciente);
			sextante2.setArcada(arcada);
			sextante2.setNome(NomeSextante.S2);
			sextante2.setDentes(denteService.criarDentes(paciente, sextante2));
			
			Sextante sextante3 = new Sextante();
			sextante3.setPaciente(paciente);
			sextante3.setArcada(arcada);
			sextante3.setNome(NomeSextante.S3);
			sextante3.setDentes(denteService.criarDentes(paciente, sextante3));

			sextantes.add(sextante1);
			sextantes.add(sextante2);
			sextantes.add(sextante3);
				
		}
		if(arcada.getTipoArcada().equals(TipoArcada.INFERIOR)){
			Sextante sextante4 = new Sextante();
			sextante4.setPaciente(paciente);
			sextante4.setArcada(arcada);
			sextante4.setNome(NomeSextante.S4);
			sextante4.setDentes(denteService.criarDentes(paciente, sextante4));
			
			Sextante sextante5 = new Sextante();
			sextante5.setPaciente(paciente);
			sextante5.setArcada(arcada);
			sextante5.setNome(NomeSextante.S5);
			sextante5.setDentes(denteService.criarDentes(paciente, sextante5));
			
			Sextante sextante6 = new Sextante();
			sextante6.setPaciente(paciente);
			sextante6.setArcada(arcada);
			sextante6.setNome(NomeSextante.S6);
			sextante6.setDentes(denteService.criarDentes(paciente, sextante6));

			sextantes.add(sextante4);
			sextantes.add(sextante5);
			sextantes.add(sextante6);
		}
		
		return sextantes;
	}

}
