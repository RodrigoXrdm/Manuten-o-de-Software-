package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.model.Registro;
import ufc.npi.prontuario.model.Registro.Acao;

public interface RegistroService {
	
	public Registro salvar(Integer idUsuario, Integer idPaciente, Integer valor, Acao acao, String observacao);
	
	List<Registro> buscarResumoRegistros();
	
	public String formatarObservacaoHtml(String observacao);
}
