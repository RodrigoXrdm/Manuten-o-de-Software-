package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Servidor;

public interface AtendimentoService {

	List<Atendimento> buscarTudoPorAluno(Aluno aluno);

	Atendimento buscarPorId(Integer id);

	List<Atendimento> buscarAtendimentosPorProfessor(Servidor servidor);

	List<Atendimento> buscarAtendimentosPorUsuario(Integer idUsuario, Paciente paciente);

	Atendimento buscarAtendimentoPorStatusEmAndamento(Paciente paciente);

	List<Atendimento> buscarAtendimentosValidadosPorDataDesc(Paciente paciente);

	Atendimento buscarUltimoPorPacienteOdontograma(Paciente paciente);

	void salvar(Atendimento atendimento) throws ProntuarioException;

	void atualizar(Atendimento atendimento) throws ProntuarioException;

	void remover(Atendimento atendimento);

	void validarAtendimento(Atendimento atendimento);

	void finalizarAtendimento(Atendimento atendimento);

	Atendimento buscarUltimoAtendimentoAbertoPorAlunoEPaciente(Aluno aluno, Paciente paciente);

	Boolean existeAtendimentoAbertoAlunoPaciente(Aluno aluno, Paciente paciente);

	List<Atendimento> buscarAtendimentoPorPaciente(Paciente paciente);

	void adicionarOdontograma(Atendimento atendimento);

	void adicionarPeriograma(Atendimento atendimento) throws ProntuarioException;

	Atendimento adicionarAvaliacaoAtendimento(Atendimento atendimento);
}
