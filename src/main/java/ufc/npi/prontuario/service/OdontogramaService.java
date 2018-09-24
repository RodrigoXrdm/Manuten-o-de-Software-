package ufc.npi.prontuario.service;

import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Odontograma;

public interface OdontogramaService {

	public Odontograma buscarPorId(Integer id);

	public Odontograma buscarPorAtendimento(Atendimento atendimento);

	public void salvar(Odontograma odontograma);

	public Odontograma adicionar(Atendimento atendimento);
}
