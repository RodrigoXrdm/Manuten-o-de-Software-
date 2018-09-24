package ufc.npi.prontuario.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Sextante;
import ufc.npi.prontuario.model.enums.NomeSextante;
import ufc.npi.prontuario.model.enums.NumeroDente;
import ufc.npi.prontuario.model.enums.TipoDente;
import ufc.npi.prontuario.repository.DenteRepository;
import ufc.npi.prontuario.service.DenteService;
import ufc.npi.prontuario.service.FaceService;
import ufc.npi.prontuario.service.FurcaService;
import ufc.npi.prontuario.service.RaizService;

@Service
public class DenteServiceImpl implements DenteService{

	@Autowired
	private DenteRepository denteRepository;
	
	@Autowired
	private FaceService faceService;
	
	@Autowired
	private RaizService raizService;
	
	@Autowired
	private FurcaService furcaService;
	
	@Override
	public List<Dente> getByPaciente(Paciente paciente) {
		List<Dente> dentes = denteRepository.getByPaciente(paciente);
		return dentes;
	}

	@Override
	public List<Dente> criarDentes(Paciente paciente, Sextante sextante){
		List<Dente> dentes = new ArrayList<>();
		
		if(sextante.getNome().equals(NomeSextante.S1)){
			Dente dente18 = new Dente();
			dente18.setPaciente(paciente);
			dente18.setSextante(sextante);
			dente18.setPresente(true);
			dente18.setImplante(false);
			dente18.setTipo(TipoDente.PERMANENTE);
			dente18.setNumero(NumeroDente.D18);
			//denteRepository.save(dente18);
			dente18.setFaces(faceService.criarFaces(paciente, dente18));
			dente18.setRaizes(raizService.criarRaizes(paciente, dente18));
			furcaService.criarFurcas(dente18);
			
			Dente dente17 = new Dente();
			dente17.setPaciente(paciente);
			dente17.setSextante(sextante);
			dente17.setPresente(true);
			dente17.setImplante(false);
			dente17.setTipo(TipoDente.PERMANENTE);
			dente17.setNumero(NumeroDente.D17);
			//denteRepository.save(dente17);
			dente17.setFaces(faceService.criarFaces(paciente, dente17));
			dente17.setRaizes(raizService.criarRaizes(paciente, dente17));
			furcaService.criarFurcas(dente17);
			
			Dente dente16 = new Dente();
			dente16.setPaciente(paciente);
			dente16.setSextante(sextante);
			dente16.setPresente(true);
			dente16.setImplante(false);
			dente16.setTipo(TipoDente.PERMANENTE);
			dente16.setNumero(NumeroDente.D16);
			//denteRepository.save(dente16);
			dente16.setFaces(faceService.criarFaces(paciente, dente16));
			dente16.setRaizes(raizService.criarRaizes(paciente, dente16));
			
			furcaService.criarFurcas(dente16);
			
			Dente dente15 = new Dente();
			dente15.setPaciente(paciente);
			dente15.setSextante(sextante);
			dente15.setPresente(true);
			dente15.setImplante(false);
			dente15.setTipo(TipoDente.PERMANENTE);
			dente15.setNumero(NumeroDente.D15);
			//denteRepository.save(dente15);
			dente15.setFaces(faceService.criarFaces(paciente, dente15));
			dente15.setRaizes(raizService.criarRaizes(paciente, dente15));
			
			Dente dente14 = new Dente();
			dente14.setPaciente(paciente);
			dente14.setSextante(sextante);
			dente14.setPresente(true);
			dente14.setImplante(false);
			dente14.setTipo(TipoDente.PERMANENTE);
			dente14.setNumero(NumeroDente.D14);
			//denteRepository.save(dente14);
			dente14.setFaces(faceService.criarFaces(paciente, dente14));
			dente14.setRaizes(raizService.criarRaizes(paciente, dente14));
			furcaService.criarFurcas(dente14);
			
			// DENTES DA ARCADA DECÍDUA
			Dente dente55 = new Dente();
			dente55.setPaciente(paciente);
			dente55.setSextante(sextante);
			dente55.setPresente(true);
			dente55.setImplante(false);
			dente55.setTipo(TipoDente.DECIDUO);
			dente55.setNumero(NumeroDente.D55);
			//denteRepository.save(dente55);
			dente55.setFaces(faceService.criarFaces(paciente, dente55));
			dente55.setRaizes(raizService.criarRaizes(paciente, dente55));
			
			Dente dente54 = new Dente();
			dente54.setPaciente(paciente);
			dente54.setSextante(sextante);
			dente54.setPresente(true);
			dente54.setImplante(false);
			dente54.setTipo(TipoDente.DECIDUO);
			dente54.setNumero(NumeroDente.D54);
			//denteRepository.save(dente54);
			dente54.setFaces(faceService.criarFaces(paciente, dente54));
			dente54.setRaizes(raizService.criarRaizes(paciente, dente54));
			
			dentes.add(dente18);
			dentes.add(dente17);
			dentes.add(dente16);
			dentes.add(dente15);
			dentes.add(dente14);
			
			dentes.add(dente55);
			dentes.add(dente54);
		} else if (sextante.getNome().equals(NomeSextante.S2)){
			Dente dente13 = new Dente();
			dente13.setPaciente(paciente);
			dente13.setSextante(sextante);
			dente13.setPresente(true);
			dente13.setImplante(false);
			dente13.setTipo(TipoDente.PERMANENTE);
			dente13.setNumero(NumeroDente.D13);
			//denteRepository.save(dente13);
			dente13.setFaces(faceService.criarFaces(paciente, dente13));
			dente13.setRaizes(raizService.criarRaizes(paciente, dente13));
			
			Dente dente12 = new Dente();
			dente12.setPaciente(paciente);
			dente12.setSextante(sextante);
			dente12.setPresente(true);
			dente12.setImplante(false);
			dente12.setTipo(TipoDente.PERMANENTE);
			dente12.setNumero(NumeroDente.D12);
			//denteRepository.save(dente12);
			dente12.setFaces(faceService.criarFaces(paciente, dente12));
			dente12.setRaizes(raizService.criarRaizes(paciente, dente12));
			
			Dente dente11 = new Dente();
			dente11.setPaciente(paciente);
			dente11.setSextante(sextante);
			dente11.setPresente(true);
			dente11.setImplante(false);
			dente11.setTipo(TipoDente.PERMANENTE);
			dente11.setNumero(NumeroDente.D11);
			//denteRepository.save(dente11);
			dente11.setFaces(faceService.criarFaces(paciente, dente11));
			dente11.setRaizes(raizService.criarRaizes(paciente, dente11));
			
			Dente dente21 = new Dente();
			dente21.setPaciente(paciente);
			dente21.setSextante(sextante);
			dente21.setPresente(true);
			dente21.setImplante(false);
			dente21.setTipo(TipoDente.PERMANENTE);
			dente21.setNumero(NumeroDente.D21);
			//denteRepository.save(dente21);
			dente21.setFaces(faceService.criarFaces(paciente, dente21));
			dente21.setRaizes(raizService.criarRaizes(paciente, dente21));
			
			Dente dente22 = new Dente();
			dente22.setPaciente(paciente);
			dente22.setSextante(sextante);
			dente22.setPresente(true);
			dente22.setImplante(false);
			dente22.setTipo(TipoDente.PERMANENTE);
			dente22.setNumero(NumeroDente.D22);
			//denteRepository.save(dente22);
			dente22.setFaces(faceService.criarFaces(paciente, dente22));
			dente22.setRaizes(raizService.criarRaizes(paciente, dente22));
			
			Dente dente23 = new Dente();
			dente23.setPaciente(paciente);
			dente23.setSextante(sextante);
			dente23.setPresente(true);
			dente23.setImplante(false);
			dente23.setTipo(TipoDente.PERMANENTE);
			dente23.setNumero(NumeroDente.D23);
			//denteRepository.save(dente23);
			dente23.setFaces(faceService.criarFaces(paciente, dente23));
			dente23.setRaizes(raizService.criarRaizes(paciente, dente23));
			
			// DENTES DA ARCADA DECÍDUA
			Dente dente53 = new Dente();
			dente53.setPaciente(paciente);
			dente53.setSextante(sextante);
			dente53.setPresente(true);
			dente53.setImplante(false);
			dente53.setTipo(TipoDente.DECIDUO);
			dente53.setNumero(NumeroDente.D53);
			//denteRepository.save(dente53);
			dente53.setFaces(faceService.criarFaces(paciente, dente53));
			dente53.setRaizes(raizService.criarRaizes(paciente, dente53));
			
			Dente dente52 = new Dente();
			dente52.setPaciente(paciente);
			dente52.setSextante(sextante);
			dente52.setPresente(true);
			dente52.setImplante(false);
			dente52.setTipo(TipoDente.DECIDUO);
			dente52.setNumero(NumeroDente.D52);
			//denteRepository.save(dente52);
			dente52.setFaces(faceService.criarFaces(paciente, dente52));
			dente52.setRaizes(raizService.criarRaizes(paciente, dente52));
			
			Dente dente51 = new Dente();
			dente51.setPaciente(paciente);
			dente51.setSextante(sextante);
			dente51.setPresente(true);
			dente51.setImplante(false);
			dente51.setTipo(TipoDente.DECIDUO);
			dente51.setNumero(NumeroDente.D51);
			//denteRepository.save(dente51);
			dente51.setFaces(faceService.criarFaces(paciente, dente51));
			dente51.setRaizes(raizService.criarRaizes(paciente, dente51));
			
			Dente dente61 = new Dente();
			dente61.setPaciente(paciente);
			dente61.setSextante(sextante);
			dente61.setPresente(true);
			dente61.setImplante(false);
			dente61.setTipo(TipoDente.DECIDUO);
			dente61.setNumero(NumeroDente.D61);
			//denteRepository.save(dente61);
			dente61.setFaces(faceService.criarFaces(paciente, dente61));
			dente61.setRaizes(raizService.criarRaizes(paciente, dente61));
			
			Dente dente62 = new Dente();
			dente62.setPaciente(paciente);
			dente62.setSextante(sextante);
			dente62.setPresente(true);
			dente62.setImplante(false);
			dente62.setTipo(TipoDente.DECIDUO);
			dente62.setNumero(NumeroDente.D62);
			//denteRepository.save(dente62);
			dente62.setFaces(faceService.criarFaces(paciente, dente62));
			dente62.setRaizes(raizService.criarRaizes(paciente, dente62));
			
			Dente dente63 = new Dente();
			dente63.setPaciente(paciente);
			dente63.setSextante(sextante);
			dente63.setPresente(true);
			dente63.setImplante(false);
			dente63.setTipo(TipoDente.DECIDUO);
			dente63.setNumero(NumeroDente.D63);
			//denteRepository.save(dente63);
			dente63.setFaces(faceService.criarFaces(paciente, dente63));
			dente63.setRaizes(raizService.criarRaizes(paciente, dente63));
			
			dentes.add(dente13);
			dentes.add(dente12);
			dentes.add(dente11);
			dentes.add(dente21);
			dentes.add(dente22);
			dentes.add(dente23);
			
			dentes.add(dente53);
			dentes.add(dente52);
			dentes.add(dente51);
			dentes.add(dente61);
			dentes.add(dente62);
			dentes.add(dente63);
		} else if (sextante.getNome().equals(NomeSextante.S3)){
			Dente dente24 = new Dente();
			dente24.setPaciente(paciente);
			dente24.setSextante(sextante);
			dente24.setPresente(true);
			dente24.setImplante(false);
			dente24.setTipo(TipoDente.PERMANENTE);
			dente24.setNumero(NumeroDente.D24);
			//denteRepository.save(dente24);
			dente24.setFaces(faceService.criarFaces(paciente, dente24));
			dente24.setRaizes(raizService.criarRaizes(paciente, dente24));
			furcaService.criarFurcas(dente24);
			
			Dente dente25 = new Dente();
			dente25.setPaciente(paciente);
			dente25.setSextante(sextante);
			dente25.setPresente(true);
			dente25.setImplante(false);
			dente25.setTipo(TipoDente.PERMANENTE);
			dente25.setNumero(NumeroDente.D25);
			//denteRepository.save(dente25);
			dente25.setFaces(faceService.criarFaces(paciente, dente25));
			dente25.setRaizes(raizService.criarRaizes(paciente, dente25));
			
			Dente dente26 = new Dente();
			dente26.setPaciente(paciente);
			dente26.setSextante(sextante);
			dente26.setPresente(true);
			dente26.setImplante(false);
			dente26.setTipo(TipoDente.PERMANENTE);
			dente26.setNumero(NumeroDente.D26);
			//denteRepository.save(dente26);
			dente26.setFaces(faceService.criarFaces(paciente, dente26));
			dente26.setRaizes(raizService.criarRaizes(paciente, dente26));
			furcaService.criarFurcas(dente26);
			
			Dente dente27 = new Dente();
			dente27.setPaciente(paciente);
			dente27.setSextante(sextante);
			dente27.setPresente(true);
			dente27.setImplante(false);
			dente27.setTipo(TipoDente.PERMANENTE);
			dente27.setNumero(NumeroDente.D27);
			//denteRepository.save(dente27);
			dente27.setFaces(faceService.criarFaces(paciente, dente27));
			dente27.setRaizes(raizService.criarRaizes(paciente, dente27));
			furcaService.criarFurcas(dente27);
			
			Dente dente28 = new Dente();
			dente28.setPaciente(paciente);
			dente28.setSextante(sextante);
			dente28.setPresente(true);
			dente28.setImplante(false);
			dente28.setTipo(TipoDente.PERMANENTE);
			dente28.setNumero(NumeroDente.D28);
			//denteRepository.save(dente28);
			dente28.setFaces(faceService.criarFaces(paciente, dente28));
			dente28.setRaizes(raizService.criarRaizes(paciente, dente28));
			furcaService.criarFurcas(dente28);
			
			// DENTES DA ARCADA DECÍDUA
			Dente dente64 = new Dente();
			dente64.setPaciente(paciente);
			dente64.setSextante(sextante);
			dente64.setPresente(true);
			dente64.setImplante(false);
			dente64.setTipo(TipoDente.DECIDUO);
			dente64.setNumero(NumeroDente.D64);
			//denteRepository.save(dente64);
			dente64.setFaces(faceService.criarFaces(paciente, dente64));
			dente64.setRaizes(raizService.criarRaizes(paciente, dente64));
			
			Dente dente65 = new Dente();
			dente65.setPaciente(paciente);
			dente65.setSextante(sextante);
			dente65.setPresente(true);
			dente65.setImplante(false);
			dente65.setTipo(TipoDente.DECIDUO);
			dente65.setNumero(NumeroDente.D65);
			//denteRepository.save(dente65);
			dente65.setFaces(faceService.criarFaces(paciente, dente65));
			dente65.setRaizes(raizService.criarRaizes(paciente, dente65));
			
			
			dentes.add(dente24);
			dentes.add(dente25);
			dentes.add(dente26);
			dentes.add(dente27);
			dentes.add(dente28);
			
			dentes.add(dente64);
			dentes.add(dente65);
		} else if (sextante.getNome().equals(NomeSextante.S4)){
			Dente dente48 = new Dente();
			dente48.setPaciente(paciente);
			dente48.setSextante(sextante);
			dente48.setPresente(true);
			dente48.setImplante(false);
			dente48.setTipo(TipoDente.PERMANENTE);
			dente48.setNumero(NumeroDente.D48);
			//denteRepository.save(dente48);
			dente48.setFaces(faceService.criarFaces(paciente, dente48));
			dente48.setRaizes(raizService.criarRaizes(paciente, dente48));
			furcaService.criarFurcas(dente48);
			
			Dente dente47 = new Dente();
			dente47.setPaciente(paciente);
			dente47.setSextante(sextante);
			dente47.setPresente(true);
			dente47.setImplante(false);
			dente47.setTipo(TipoDente.PERMANENTE);
			dente47.setNumero(NumeroDente.D47);
			//denteRepository.save(dente47);
			dente47.setFaces(faceService.criarFaces(paciente, dente47));
			dente47.setRaizes(raizService.criarRaizes(paciente, dente47));
			furcaService.criarFurcas(dente47);
			
			Dente dente46 = new Dente();
			dente46.setPaciente(paciente);
			dente46.setSextante(sextante);
			dente46.setPresente(true);
			dente46.setImplante(false);
			dente46.setTipo(TipoDente.PERMANENTE);
			dente46.setNumero(NumeroDente.D46);
			//denteRepository.save(dente46);
			dente46.setFaces(faceService.criarFaces(paciente, dente46));
			dente46.setRaizes(raizService.criarRaizes(paciente, dente46));
			furcaService.criarFurcas(dente46);
			
			Dente dente45 = new Dente();
			dente45.setPaciente(paciente);
			dente45.setSextante(sextante);
			dente45.setPresente(true);
			dente45.setImplante(false);
			dente45.setTipo(TipoDente.PERMANENTE);
			dente45.setNumero(NumeroDente.D45);
			//denteRepository.save(dente45);
			dente45.setFaces(faceService.criarFaces(paciente, dente45));
			dente45.setRaizes(raizService.criarRaizes(paciente, dente45));
			
			Dente dente44 = new Dente();
			dente44.setPaciente(paciente);
			dente44.setSextante(sextante);
			dente44.setPresente(true);
			dente44.setImplante(false);
			dente44.setTipo(TipoDente.PERMANENTE);
			dente44.setNumero(NumeroDente.D44);
			//denteRepository.save(dente44);
			dente44.setFaces(faceService.criarFaces(paciente, dente44));
			dente44.setRaizes(raizService.criarRaizes(paciente, dente44));
			
			// DENTES DA ARCADA DECÍDUA
			Dente dente85 = new Dente();
			dente85.setPaciente(paciente);
			dente85.setSextante(sextante);
			dente85.setPresente(true);
			dente85.setImplante(false);
			dente85.setTipo(TipoDente.DECIDUO);
			dente85.setNumero(NumeroDente.D85);
			//denteRepository.save(dente85);
			dente85.setFaces(faceService.criarFaces(paciente, dente85));
			dente85.setRaizes(raizService.criarRaizes(paciente, dente85));
			
			Dente dente84 = new Dente();
			dente84.setPaciente(paciente);
			dente84.setSextante(sextante);
			dente84.setPresente(true);
			dente84.setImplante(false);
			dente84.setTipo(TipoDente.DECIDUO);
			dente84.setNumero(NumeroDente.D84);
			//denteRepository.save(dente84);
			dente84.setFaces(faceService.criarFaces(paciente, dente84));
			dente84.setRaizes(raizService.criarRaizes(paciente, dente84));
			
			dentes.add(dente48);
			dentes.add(dente47);
			dentes.add(dente46);
			dentes.add(dente45);
			dentes.add(dente44);
			
			dentes.add(dente85);
			dentes.add(dente84);
		} else if (sextante.getNome().equals(NomeSextante.S5)){
			Dente dente43 = new Dente();
			dente43.setPaciente(paciente);
			dente43.setSextante(sextante);
			dente43.setPresente(true);
			dente43.setImplante(false);
			dente43.setTipo(TipoDente.PERMANENTE);
			dente43.setNumero(NumeroDente.D43);
			//denteRepository.save(dente43);
			dente43.setFaces(faceService.criarFaces(paciente, dente43));
			dente43.setRaizes(raizService.criarRaizes(paciente, dente43));
			
			Dente dente42 = new Dente();
			dente42.setPaciente(paciente);
			dente42.setSextante(sextante);
			dente42.setPresente(true);
			dente42.setImplante(false);
			dente42.setTipo(TipoDente.PERMANENTE);
			dente42.setNumero(NumeroDente.D42);
			//denteRepository.save(dente42);
			dente42.setFaces(faceService.criarFaces(paciente, dente42));
			dente42.setRaizes(raizService.criarRaizes(paciente, dente42));
			
			Dente dente41 = new Dente();
			dente41.setPaciente(paciente);
			dente41.setSextante(sextante);
			dente41.setPresente(true);
			dente41.setImplante(false);
			dente41.setTipo(TipoDente.PERMANENTE);
			dente41.setNumero(NumeroDente.D41);
			//denteRepository.save(dente41);
			dente41.setFaces(faceService.criarFaces(paciente, dente41));
			dente41.setRaizes(raizService.criarRaizes(paciente, dente41));
			
			Dente dente31 = new Dente();
			dente31.setPaciente(paciente);
			dente31.setSextante(sextante);
			dente31.setPresente(true);
			dente31.setImplante(false);
			dente31.setTipo(TipoDente.PERMANENTE);
			dente31.setNumero(NumeroDente.D31);
			//denteRepository.save(dente31);
			dente31.setFaces(faceService.criarFaces(paciente, dente31));
			dente31.setRaizes(raizService.criarRaizes(paciente, dente31));
			
			Dente dente32 = new Dente();
			dente32.setPaciente(paciente);
			dente32.setSextante(sextante);
			dente32.setPresente(true);
			dente32.setImplante(false);
			dente32.setTipo(TipoDente.PERMANENTE);
			dente32.setNumero(NumeroDente.D32);
			//denteRepository.save(dente32);
			dente32.setFaces(faceService.criarFaces(paciente, dente32));
			dente32.setRaizes(raizService.criarRaizes(paciente, dente32));
			
			Dente dente33 = new Dente();
			dente33.setPaciente(paciente);
			dente33.setSextante(sextante);
			dente33.setPresente(true);
			dente33.setImplante(false);
			dente33.setTipo(TipoDente.PERMANENTE);
			dente33.setNumero(NumeroDente.D33);
			//denteRepository.save(dente33);
			dente33.setFaces(faceService.criarFaces(paciente, dente33));
			dente33.setRaizes(raizService.criarRaizes(paciente, dente33));
			
			// DENTES DA ARCADA DECÍDUA
			Dente dente83 = new Dente();
			dente83.setPaciente(paciente);
			dente83.setSextante(sextante);
			dente83.setPresente(true);
			dente83.setImplante(false);
			dente83.setTipo(TipoDente.DECIDUO);
			dente83.setNumero(NumeroDente.D83);
			//denteRepository.save(dente83);
			dente83.setFaces(faceService.criarFaces(paciente, dente83));
			dente83.setRaizes(raizService.criarRaizes(paciente, dente83));
			
			Dente dente82 = new Dente();
			dente82.setPaciente(paciente);
			dente82.setSextante(sextante);
			dente82.setPresente(true);
			dente82.setImplante(false);
			dente82.setTipo(TipoDente.DECIDUO);
			dente82.setNumero(NumeroDente.D82);
			//denteRepository.save(dente82);
			dente82.setFaces(faceService.criarFaces(paciente, dente82));
			dente82.setRaizes(raizService.criarRaizes(paciente, dente82));
			
			Dente dente81 = new Dente();
			dente81.setPaciente(paciente);
			dente81.setSextante(sextante);
			dente81.setPresente(true);
			dente81.setImplante(false);
			dente81.setTipo(TipoDente.DECIDUO);
			dente81.setNumero(NumeroDente.D81);
			//denteRepository.save(dente81);
			dente81.setFaces(faceService.criarFaces(paciente, dente81));
			dente81.setRaizes(raizService.criarRaizes(paciente, dente81));
			
			Dente dente71 = new Dente();
			dente71.setPaciente(paciente);
			dente71.setSextante(sextante);
			dente71.setPresente(true);
			dente71.setImplante(false);
			dente71.setTipo(TipoDente.DECIDUO);
			dente71.setNumero(NumeroDente.D71);
			//denteRepository.save(dente71);
			dente71.setFaces(faceService.criarFaces(paciente, dente71));
			dente71.setRaizes(raizService.criarRaizes(paciente, dente71));
			
			Dente dente72 = new Dente();
			dente72.setPaciente(paciente);
			dente72.setSextante(sextante);
			dente72.setPresente(true);
			dente72.setImplante(false);
			dente72.setTipo(TipoDente.DECIDUO);
			dente72.setNumero(NumeroDente.D72);
			//denteRepository.save(dente72);
			dente72.setFaces(faceService.criarFaces(paciente, dente72));
			dente72.setRaizes(raizService.criarRaizes(paciente, dente72));
			
			Dente dente73 = new Dente();
			dente73.setPaciente(paciente);
			dente73.setSextante(sextante);
			dente73.setPresente(true);
			dente73.setImplante(false);
			dente73.setTipo(TipoDente.DECIDUO);
			dente73.setNumero(NumeroDente.D73);
			//denteRepository.save(dente73);
			dente73.setFaces(faceService.criarFaces(paciente, dente73));
			dente73.setRaizes(raizService.criarRaizes(paciente, dente73));
			
			dentes.add(dente43);
			dentes.add(dente42);
			dentes.add(dente41);
			dentes.add(dente31);
			dentes.add(dente32);
			dentes.add(dente33);
			
			dentes.add(dente83);
			dentes.add(dente82);
			dentes.add(dente81);
			dentes.add(dente71);
			dentes.add(dente72);
			dentes.add(dente73);
		} else if (sextante.getNome().equals(NomeSextante.S6)){
			Dente dente34 = new Dente();
			dente34.setPaciente(paciente);
			dente34.setSextante(sextante);
			dente34.setPresente(true);
			dente34.setImplante(false);
			dente34.setTipo(TipoDente.PERMANENTE);
			dente34.setNumero(NumeroDente.D34);
			//denteRepository.save(dente34);
			dente34.setFaces(faceService.criarFaces(paciente, dente34));
			dente34.setRaizes(raizService.criarRaizes(paciente, dente34));
			
			Dente dente35 = new Dente();
			dente35.setPaciente(paciente);
			dente35.setSextante(sextante);
			dente35.setPresente(true);
			dente35.setImplante(false);
			dente35.setTipo(TipoDente.PERMANENTE);
			dente35.setNumero(NumeroDente.D35);
			//denteRepository.save(dente35);
			dente35.setFaces(faceService.criarFaces(paciente, dente35));
			dente35.setRaizes(raizService.criarRaizes(paciente, dente35));
			
			Dente dente36 = new Dente();
			dente36.setPaciente(paciente);
			dente36.setSextante(sextante);
			dente36.setPresente(true);
			dente36.setImplante(false);
			dente36.setTipo(TipoDente.PERMANENTE);
			dente36.setNumero(NumeroDente.D36);
			//denteRepository.save(dente36);
			dente36.setFaces(faceService.criarFaces(paciente, dente36));
			dente36.setRaizes(raizService.criarRaizes(paciente, dente36));
			furcaService.criarFurcas(dente36);
			
			Dente dente37 = new Dente();
			dente37.setPaciente(paciente);
			dente37.setSextante(sextante);
			dente37.setPresente(true);
			dente37.setImplante(false);
			dente37.setTipo(TipoDente.PERMANENTE);
			dente37.setNumero(NumeroDente.D37);
			//denteRepository.save(dente37);
			dente37.setFaces(faceService.criarFaces(paciente, dente37));
			dente37.setRaizes(raizService.criarRaizes(paciente, dente37));
			furcaService.criarFurcas(dente37);
			
			Dente dente38 = new Dente();
			dente38.setPaciente(paciente);
			dente38.setSextante(sextante);
			dente38.setPresente(true);
			dente38.setImplante(false);
			dente38.setTipo(TipoDente.PERMANENTE);
			dente38.setNumero(NumeroDente.D38);
			//denteRepository.save(dente38);
			dente38.setFaces(faceService.criarFaces(paciente, dente38));
			dente38.setRaizes(raizService.criarRaizes(paciente, dente38));
			furcaService.criarFurcas(dente38);
			
			// DENTES DA ARCADA DECÍDUA
			Dente dente74 = new Dente();
			dente74.setPaciente(paciente);
			dente74.setSextante(sextante);
			dente74.setPresente(true);
			dente74.setImplante(false);
			dente74.setTipo(TipoDente.DECIDUO);
			dente74.setNumero(NumeroDente.D74);
			//denteRepository.save(dente74);
			dente74.setFaces(faceService.criarFaces(paciente, dente74));
			dente74.setRaizes(raizService.criarRaizes(paciente, dente74));
			
			Dente dente75 = new Dente();
			dente75.setPaciente(paciente);
			dente75.setSextante(sextante);
			dente75.setPresente(true);
			dente75.setImplante(false);
			dente75.setTipo(TipoDente.DECIDUO);
			dente75.setNumero(NumeroDente.D75);
			//denteRepository.save(dente75);
			dente75.setFaces(faceService.criarFaces(paciente, dente75));
			dente75.setRaizes(raizService.criarRaizes(paciente, dente75));
			furcaService.criarFurcas(dente75);
			
//			Dente dente76 = new Dente();
//			dente76.setPaciente(paciente);
//			dente76.setSextante(sextante);
//			dente76.setPresente(true);
//			dente76.setImplante(false);
//			dente76.setTipo(TipoDente.DECIDUO);
//			dente76.setNumero(NumeroDente.D75);
//			//denteRepository.save(dente76);
//			dente76.setFaces(faceService.criarFaces(paciente, dente76));
//			dente76.setRaizes(raizService.criarRaizes(paciente, dente76));
//			furcaService.criarFurcas(dente76);
			
			dentes.add(dente34);
			dentes.add(dente35);
			dentes.add(dente36);
			dentes.add(dente37);
			dentes.add(dente38);
			
			dentes.add(dente74);
			dentes.add(dente75);
		}
		return dentes;
	}

}
