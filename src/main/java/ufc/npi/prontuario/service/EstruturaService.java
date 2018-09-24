package ufc.npi.prontuario.service;

import ufc.npi.prontuario.model.Estrutura;
import ufc.npi.prontuario.model.Paciente;

public interface EstruturaService {
	void salvar(Estrutura estrutura);

	public Estrutura buscarPorId(Integer idEstrutura);

	void criarEstruturaBucal(Paciente paciente);
}
