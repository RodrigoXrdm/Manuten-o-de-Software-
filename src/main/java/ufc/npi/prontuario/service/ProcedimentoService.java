package ufc.npi.prontuario.service;

import java.util.Date;
import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Estrutura;
import ufc.npi.prontuario.model.Ficha;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.model.Usuario;

public interface ProcedimentoService {

	List<Procedimento> buscarPorAtendimento(Atendimento atendimento);

	List<Procedimento> buscarProcedimentosPacienteUsuarioPre(Paciente paciente, Usuario usuario, Boolean preExistente);

	List<Procedimento> buscarProcedimentosEstruturaAndamento(Estrutura estrutura, Aluno aluno, Boolean preExistente);

	List<Procedimento> buscarProcedimentosPaciente(Paciente paciente, Usuario usuario);

	void excluir(int id);

	void excluirTodosPorEstruturaEAlunoPre(Estrutura estrutura, Aluno aluno, Boolean preExistente);

	Procedimento buscarPorId(int id);

	Procedimento atualizar(Procedimento procedimento);

	List<Procedimento> salvar(Estrutura estrutura, List<Integer> idProcedimentos, Ficha ficha, String descricao,
			Aluno aluno, Boolean preExistente, List<Integer> patologias, Date data) throws ProntuarioException;

}
