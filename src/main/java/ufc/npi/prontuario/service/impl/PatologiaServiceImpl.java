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
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.model.Tratamento;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.model.enums.Papel;
import ufc.npi.prontuario.model.enums.StatusAtendimento;
import ufc.npi.prontuario.repository.AlunoRepository;
import ufc.npi.prontuario.repository.PatologiaRepository;
import ufc.npi.prontuario.repository.ServidorRepository;
import ufc.npi.prontuario.repository.TipoPatologiaRepository;
import ufc.npi.prontuario.service.EstruturaService;
import ufc.npi.prontuario.service.PatologiaService;

@Service
public class PatologiaServiceImpl implements PatologiaService {

	@Autowired
	private PatologiaRepository patologiaRepository;

	@Autowired
	private TipoPatologiaRepository tipoPatologiaRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private ServidorRepository servidorRepository;

	@Autowired
	private EstruturaService estruturaService;

	@Override
	public List<Patologia> salvar(Estrutura estrutura, List<Integer> idPatologias, Ficha ficha, String descricao,
			Aluno aluno) throws ProntuarioException {

		if (estruturaService.buscarPorId(estrutura.getId()) == null) {
			estruturaService.criarEstruturaBucal(ficha.getAtendimento().getPaciente());
		}

		List<Patologia> patologias = new ArrayList<Patologia>();
		patologias = salvarPatologias(idPatologias, estrutura, ficha, descricao, patologias);
		return patologias;
	}

	private List<Patologia> salvarPatologias(List<Integer> idPatologias, Estrutura estrutura, Ficha ficha,
			String descricao, List<Patologia> patologias) {

		for (Integer idPatologia : idPatologias) {
			TipoPatologia tipo = tipoPatologiaRepository.findOne(idPatologia);
			Patologia patologia = new Patologia();
			patologia.setEstrutura(estrutura);
			patologia.setTipo(tipo);
			patologia.setData(new Date());
			patologia.setFicha(ficha);
			patologia.setDescricao(descricao);
			patologiaRepository.save(patologia);
			patologias.add(patologia);
		}

		return patologias;
	}

	@Override
	public Patologia tratar(Patologia patologia, Tratamento tratamento) {
		patologia.setTratamento(tratamento);
		patologiaRepository.saveAndFlush(patologia);
		return patologia;
	}

	@Override
	public Patologia buscarPorId(int id) {
		return patologiaRepository.findOne(id);
	}

	@Override
	public Patologia atualizar(Patologia patologia) {
		if (patologia != null && patologia.getId() != null) {
			if (patologiaRepository.findOne(patologia.getId()) != null) {
				patologiaRepository.save(patologia);
				return patologia;
			}
		}
		return null;
	}

	@Override
	public List<Patologia> buscarPorAtendimento(Atendimento atendimento) {
		return patologiaRepository.buscarPorAtendimento(atendimento);
	}

	@Override
	public List<Patologia> buscarTodasPatologiasDoPaciente(Paciente paciente) {
		List<Patologia> patologias = patologiaRepository.buscarPorPaciente(paciente);
		return patologias;
	}

	@Override
	public void excluir(Integer id) {
		patologiaRepository.delete(id);

	}

	@Override
	public void excluirTudoPorEstruturaEAluno(Estrutura estrutura, Aluno aluno) {
		patologiaRepository.apagarPorEstruturaEAlunoEStatus(estrutura, aluno, StatusAtendimento.EM_ANDAMENTO);
	}

	@Override
	public List<Patologia> buscarPatologiasPorEstruturaAlunoEAtendimentoEmAndamento(Estrutura estrutura, Aluno aluno) {
		List<Patologia> patologias = patologiaRepository.buscarPatologiaEstruturaAndamento(estrutura, aluno,
				StatusAtendimento.EM_ANDAMENTO);
		return patologias;
	}

	@Override
	public List<Patologia> buscarPatologiasDoPacientePorUsuario(Paciente paciente, Usuario usuario) {
		Aluno aluno = alunoRepository.findOne(usuario.getId());
		Servidor servidor = servidorRepository.findOne(usuario.getId());

		List<Patologia> patologias = new ArrayList<>();

		if (aluno != null) {
			patologias = patologiaRepository.buscarPorResponsavelOuAjudante(paciente, aluno);
		} else if (servidor != null) {
			patologias = patologiaRepository.findAllByPacienteAndAtendimentoProfessor(paciente, servidor);
		}

		if (usuario.getPapeis().contains(Papel.ADMINISTRACAO)) {
			patologias = patologiaRepository.buscarPorPaciente(paciente);
		}

		patologias.addAll(patologiaRepository.buscarPorPacienteEStatus(paciente, StatusAtendimento.VALIDADO));

		Collections.sort(patologias);

		return patologias;
	}
}
