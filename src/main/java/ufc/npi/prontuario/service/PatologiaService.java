package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Estrutura;
import ufc.npi.prontuario.model.Ficha;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Tratamento;
import ufc.npi.prontuario.model.Usuario;

public interface PatologiaService {

	Patologia tratar(Patologia patologia, Tratamento tratamento);

	Patologia buscarPorId(int id);

	Patologia atualizar(Patologia patologia);

	List<Patologia> buscarPorAtendimento(Atendimento atendimento);

	List<Patologia> buscarPatologiasPorEstruturaAlunoEAtendimentoEmAndamento(Estrutura estrutura, Aluno aluno);

	List<Patologia> buscarPatologiasDoPacientePorUsuario(Paciente paciente, Usuario usuario);

	List<Patologia> buscarTodasPatologiasDoPaciente(Paciente paciente);

	void excluir(Integer id);

	void excluirTudoPorEstruturaEAluno(Estrutura estrutura, Aluno aluno);

	List<Patologia> salvar(Estrutura estrutura, List<Integer> idPatologias, Ficha ficha, String descricao, Aluno aluno)
			throws ProntuarioException;

}
