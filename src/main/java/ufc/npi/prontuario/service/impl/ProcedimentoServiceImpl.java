package ufc.npi.prontuario.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Estrutura;
import ufc.npi.prontuario.model.Ficha;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.TipoProcedimento;
import ufc.npi.prontuario.model.Tratamento;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.model.enums.Papel;
import ufc.npi.prontuario.model.enums.StatusAtendimento;
import ufc.npi.prontuario.repository.AlunoRepository;
import ufc.npi.prontuario.repository.PatologiaRepository;
import ufc.npi.prontuario.repository.ProcedimentoRepository;
import ufc.npi.prontuario.repository.ServidorRepository;
import ufc.npi.prontuario.repository.TipoProcedimentoRepository;
import ufc.npi.prontuario.service.EstruturaService;
import ufc.npi.prontuario.service.ProcedimentoService;

@Service
public class ProcedimentoServiceImpl implements ProcedimentoService {

	@Autowired
	private ProcedimentoRepository procedimentoRepository;

	@Autowired
	private TipoProcedimentoRepository tipoProcedimentoRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private ServidorRepository servidorRepository;

	@Autowired
	private PatologiaRepository patologiaRepository;

	@Autowired
	private EstruturaService estruturaService;

	@Override
	public List<Procedimento> salvar(Estrutura estrutura, List<Integer> idProcedimentos, Ficha ficha, String descricao,
			Aluno aluno, Boolean preExistente, List<Integer> patologias, Date data) throws ProntuarioException {
		List<Procedimento> procedimentos = new ArrayList<Procedimento>();

		if (estruturaService.buscarPorId(estrutura.getId()) == null) {
			estruturaService.criarEstruturaBucal(ficha.getAtendimento().getPaciente());
		}

		procedimentos = salvarProcedimentos(estrutura, idProcedimentos, ficha, descricao, procedimentos, preExistente,
				data);
		if (patologias != null && !patologias.isEmpty()) {
			Tratamento tratamento = new Tratamento();
			tratamento.setData(data);
			tratamento.setDescricao(descricao);
			tratamento.setResponsavel(aluno);

			for (Integer p : patologias) {
				Patologia patologia = patologiaRepository.findOne(p);
				tratamento.setPatologia(patologia);
				patologia.setTratamento(tratamento);
				patologiaRepository.saveAndFlush(patologia);
			}
		}
		return procedimentos;
	}

	private List<Procedimento> salvarProcedimentos(Estrutura estrutura, List<Integer> idProcedimentos, Ficha ficha,
			String descricao, List<Procedimento> procedimentos, Boolean preExistente, Date data) {

		for (Integer p : idProcedimentos) {
			TipoProcedimento tipo = tipoProcedimentoRepository.findOne(p);

			Procedimento procedimento = new Procedimento();
			procedimento.setEstrutura(estrutura);
			procedimento.setDescricao(descricao);
			procedimento.setTipoProcedimento(tipo);
			procedimento.setData(data);
			procedimento.setFicha(ficha);
			procedimento.setPreExistente(preExistente);

			procedimentoRepository.save(procedimento);

			procedimentos.add(procedimento);
		}

		return procedimentos;
	}

	@Override
	public List<Procedimento> buscarPorAtendimento(Atendimento atendimento) {
		return procedimentoRepository.buscarPorAtendimento(atendimento);
	}

	@Override
	public List<Procedimento> buscarProcedimentosPacienteUsuarioPre(Paciente paciente, Usuario usuario,
			Boolean preExistente) {
		Aluno aluno = alunoRepository.findOne(usuario.getId());
		Servidor servidor = servidorRepository.findOne(usuario.getId());

		List<Procedimento> procedimentos = new ArrayList<>();

		if (aluno != null) {
			procedimentos = procedimentoRepository.buscarPorResponsavelOuAjudante(paciente, aluno, preExistente);
		} else if (servidor != null) {
			procedimentos = procedimentoRepository.findAllByPacienteAndAtendimentoProfessor(paciente, servidor,
					preExistente);
		}

		if (usuario.getPapeis().contains(Papel.ADMINISTRACAO)) {
			procedimentos = procedimentoRepository.buscarPorPaciente(paciente, preExistente);
		}

		procedimentos.addAll(
				procedimentoRepository.buscarPorPacienteEStatus(paciente, StatusAtendimento.VALIDADO, preExistente));

		Collections.sort(procedimentos);
		return procedimentos;
	}

	@Override
	public List<Procedimento> buscarProcedimentosEstruturaAndamento(Estrutura estrutura, Aluno aluno,
			Boolean preExistente) {
		List<Procedimento> procedimentos = procedimentoRepository.findAllByEstruturaAndAlunoAndPre(estrutura, aluno,
				preExistente, StatusAtendimento.EM_ANDAMENTO);
		return procedimentos;
	}

	@Override
	public List<Procedimento> buscarProcedimentosPaciente(Paciente paciente, Usuario usuario) {
		Aluno aluno = alunoRepository.findOne(usuario.getId());
		Servidor servidor = servidorRepository.findOne(usuario.getId());

		List<Procedimento> procedimentos = new ArrayList<>();

		if (aluno != null) {
			procedimentos = procedimentoRepository.buscarPorResponsavelOuAjudante(paciente, aluno);
		} else if (servidor != null) {
			procedimentos = procedimentoRepository.findAllByPacienteAndAtendimentoProfessor(paciente, servidor);
		}

		if (usuario.getPapeis().contains(Papel.ADMINISTRACAO)) {
			procedimentos = procedimentoRepository.buscarPorPaciente(paciente);
		}

		procedimentos.addAll(procedimentoRepository.buscarPorPacienteEStatus(paciente, StatusAtendimento.VALIDADO));

		Collections.sort(procedimentos);

		return procedimentos;
	}

	@Override
	public void excluir(int id) {
		procedimentoRepository.delete(id);
	}

	@Override
	public void excluirTodosPorEstruturaEAlunoPre(Estrutura estrutura, Aluno aluno, Boolean preExistente) {
		procedimentoRepository.deleteAllByEstruturaAndAlunoAndPreAndStatus(estrutura, aluno, preExistente,
				StatusAtendimento.EM_ANDAMENTO);

	}

	@Override
	public Procedimento buscarPorId(int id) {
		return procedimentoRepository.findOne(id);
	}

	@Override
	public Procedimento atualizar(Procedimento procedimento) {
		if (procedimento != null && procedimento.getId() != null) {
			if (procedimentoRepository.findOne(procedimento.getId()) != null) {
				procedimentoRepository.save(procedimento);
				return procedimento;
			}
		}
		return null;
	}

}
