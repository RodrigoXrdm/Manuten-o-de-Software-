package ufc.npi.prontuario.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Furca;
import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.NumeroDente;
import ufc.npi.prontuario.repository.FurcaRepository;
import ufc.npi.prontuario.service.FurcaService;

@Service
public class FurcaServiceImpl implements FurcaService{
	@Autowired
	private FurcaRepository furcaRepository;

	@Override
	public void criarFurcas(Dente dente){
		if(dente.getNumero().equals(NumeroDente.D18) ||
				dente.getNumero().equals(NumeroDente.D17) ||
				dente.getNumero().equals(NumeroDente.D16) ||
				dente.getNumero().equals(NumeroDente.D26) ||
				dente.getNumero().equals(NumeroDente.D27) ||
				dente.getNumero().equals(NumeroDente.D28)){
			Furca furca = new Furca();
			furca.setPaciente(dente.getPaciente());
			furca.setFace(NomeFace.V);

			furca.addRaiz(dente.getRaizes().get(0));
			furca.addRaiz(dente.getRaizes().get(1));
			furcaRepository.save(furca);
			
			dente.getRaizes().get(0).addFurca(furca);
			dente.getRaizes().get(1).addFurca(furca);
			
			Furca furca2 = new Furca();
			furca2.setPaciente(dente.getPaciente());
			furca2.setFace(NomeFace.D);
			
			furca2.addRaiz(dente.getRaizes().get(0));
			furca2.addRaiz(dente.getRaizes().get(2));		
			furcaRepository.save(furca2);
			
			dente.getRaizes().get(0).addFurca(furca2);
			dente.getRaizes().get(2).addFurca(furca2);
			
			Furca furca3 = new Furca();
			furca3.setPaciente(dente.getPaciente());
			furca3.setFace(NomeFace.M);
			
			furca3.addRaiz(dente.getRaizes().get(1));
			furca3.addRaiz(dente.getRaizes().get(2));
			furcaRepository.save(furca3);
			
			dente.getRaizes().get(1).addFurca(furca3);
			dente.getRaizes().get(2).addFurca(furca3);
		}
		
		if(dente.getNumero().equals(NumeroDente.D14) ||
				dente.getNumero().equals(NumeroDente.D24)){
			Furca furca = new Furca();
			furca.setPaciente(dente.getPaciente());
			furca.setFace(NomeFace.M);
			
			furca.addRaiz(dente.getRaizes().get(0));
			furca.addRaiz(dente.getRaizes().get(1));	
			furcaRepository.save(furca);
			
			dente.getRaizes().get(0).addFurca(furca);
			dente.getRaizes().get(1).addFurca(furca);
			
			
			Furca furca2 = new Furca();
			furca2.setPaciente(dente.getPaciente());
			furca2.setFace(NomeFace.D);
			
			furca2.addRaiz(dente.getRaizes().get(0));
			furca2.addRaiz(dente.getRaizes().get(1));			
			furcaRepository.save(furca2);
			
			dente.getRaizes().get(0).addFurca(furca2);
			dente.getRaizes().get(1).addFurca(furca2);
		}
		
		if(dente.getNumero().equals(NumeroDente.D48) ||
				dente.getNumero().equals(NumeroDente.D47) ||
				dente.getNumero().equals(NumeroDente.D46) ||
				dente.getNumero().equals(NumeroDente.D36) ||
				dente.getNumero().equals(NumeroDente.D37) ||
				dente.getNumero().equals(NumeroDente.D38)){
			Furca furca = new Furca();
			furca.setPaciente(dente.getPaciente());
			furca.setFace(NomeFace.L);
			
			furca.addRaiz(dente.getRaizes().get(0));
			furca.addRaiz(dente.getRaizes().get(1));
			furcaRepository.save(furca);
			
			dente.getRaizes().get(0).addFurca(furca);
			dente.getRaizes().get(1).addFurca(furca);
			
			Furca furca2 = new Furca();
			furca2.setPaciente(dente.getPaciente());
			furca2.setFace(NomeFace.V);
			
			furca2.addRaiz(dente.getRaizes().get(0));
			furca2.addRaiz(dente.getRaizes().get(1));	
			furcaRepository.save(furca2);
			
			dente.getRaizes().get(0).addFurca(furca2);
			dente.getRaizes().get(1).addFurca(furca2);
		}
	}

}
