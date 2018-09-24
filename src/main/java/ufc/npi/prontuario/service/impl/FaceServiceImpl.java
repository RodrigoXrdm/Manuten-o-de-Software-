package ufc.npi.prontuario.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Face;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.repository.FaceRepository;
import ufc.npi.prontuario.service.FaceService;

@Service
public class FaceServiceImpl implements FaceService {

	@Autowired
	private FaceRepository faceRepository;
	
	@Override
	public void salvar(Face face) {
		faceRepository.save(face);
	}

	@Override
	public Face buscarPorId(Integer idFace) {
		return faceRepository.findOne(idFace);
	}

	@Override
	public List<Face> criarFaces(Paciente paciente, Dente dente){
		List<Face> faces = new ArrayList<>();
		
		Face vestibular = new Face();
		vestibular.setPaciente(paciente);
		vestibular.setDente(dente);
		vestibular.setNome(NomeFace.V);
		
		Face lingual = new Face();
		lingual.setPaciente(paciente);
		lingual.setDente(dente);
		lingual.setNome(NomeFace.L);
		
		Face mesial = new Face();
		mesial.setPaciente(paciente);
		mesial.setDente(dente);
		mesial.setNome(NomeFace.M);
		
		Face distal = new Face();
		distal.setPaciente(paciente);
		distal.setDente(dente);
		distal.setNome(NomeFace.D);
		
		Face oclusal = new Face();
		oclusal.setPaciente(paciente);
		oclusal.setDente(dente);
		oclusal.setNome(NomeFace.O);
		
		faces.add(vestibular);
		faces.add(lingual);
		faces.add(mesial);
		faces.add(distal);
		faces.add(oclusal);
		
		return faces;
	}



}
