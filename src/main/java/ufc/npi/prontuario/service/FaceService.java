package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Face;
import ufc.npi.prontuario.model.Paciente;

public interface FaceService {
	void salvar(Face face);
	Face buscarPorId(Integer idFace);
	List<Face> criarFaces(Paciente paciente, Dente dente);
}
