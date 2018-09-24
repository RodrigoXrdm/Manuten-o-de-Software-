package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.enums.StatusPeriograma;

public interface PeriogramaService {

	Periograma adicionar(Paciente paciente, Aluno aluno, StatusPeriograma statusPeriograma) throws ProntuarioException;

	void atualizar(Periograma periograma);

	Periograma buscarPorId(Integer id);

	Periograma buscarPorAtendimento(Atendimento atendimento);

	List<Periograma> buscarPeriogramasValidados(Paciente paciente);

	boolean existePeriogramaJaCadastrado(Paciente paciente, Aluno aluno);

	Periograma adicionar(Atendimento atendimento) throws ProntuarioException;
}
