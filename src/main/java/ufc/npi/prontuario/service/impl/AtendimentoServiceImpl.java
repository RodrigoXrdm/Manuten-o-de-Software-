package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EDITAR_ATENDIMENTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Avaliacao;
import ufc.npi.prontuario.model.AvaliacaoAtendimento;
import ufc.npi.prontuario.model.Ficha;
import ufc.npi.prontuario.model.ItemAvaliacao;
import ufc.npi.prontuario.model.ItemAvaliacaoAtendimento;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.enums.Papel;
import ufc.npi.prontuario.model.enums.StatusAtendimento;
import ufc.npi.prontuario.repository.AlunoRepository;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.AvaliacaoAtendimentoRepository;
import ufc.npi.prontuario.repository.AvaliacaoRepository;
import ufc.npi.prontuario.repository.ServidorRepository;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.OdontogramaService;
import ufc.npi.prontuario.service.PeriogramaService;

@Service
public class AtendimentoServiceImpl implements AtendimentoService {

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private ServidorRepository servidorRepository;

	@Autowired
	private AvaliacaoAtendimentoRepository avaliacaoAtendimentoRepository;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private OdontogramaService odontogramaService;

	@Autowired
	private PeriogramaService periogramaService;

	@Override
	public Atendimento buscarPorId(Integer id) {
		return atendimentoRepository.findOne(id);
	}

	@Override
	public void salvar(Atendimento atendimento) throws ProntuarioException {
		List<Atendimento> atendimentos = atendimentoRepository.findAllByResponsavelOrAjudanteExist(
				atendimento.getResponsavel(), atendimento.getAjudante(), atendimento.getPaciente(),
				StatusAtendimento.EM_ANDAMENTO);

		if (atendimentos.size() >= 1) {
			throw new ProntuarioException(ERRO_ADICIONAR_ATENDIMENTO);
		}

		atendimento.setStatus(StatusAtendimento.EM_ANDAMENTO);
		atendimentoRepository.save(atendimento);
	}

	@Override
	public void atualizar(Atendimento atendimento) throws ProntuarioException {
		if (atendimentoRepository.exists(atendimento.getId())) {
			atendimentoRepository.save(atendimento);
		} else {
			throw new ProntuarioException(ERRO_EDITAR_ATENDIMENTO);
		}
	}

	@Override
	public List<Atendimento> buscarAtendimentosValidadosPorDataDesc(Paciente paciente) {
		List<Atendimento> atendimentos = atendimentoRepository
				.findAllByStatusAndPacienteOrderByDataDesc(StatusAtendimento.VALIDADO, paciente);
		return atendimentos;
	}

	@Override
	public Atendimento buscarAtendimentoPorStatusEmAndamento(Paciente paciente) {
		Atendimento atendimento = atendimentoRepository.findByStatusAndPaciente(StatusAtendimento.EM_ANDAMENTO,
				paciente);
		return atendimento;
	}

	@Override
	public List<Atendimento> buscarTudoPorAluno(Aluno aluno) {
		return atendimentoRepository.findAllByResponsavelOrAjudanteOrderByDataDesc(aluno, aluno);

	}

	@Override
	public void finalizarAtendimento(Atendimento atendimento) {
		atendimento.setStatus(StatusAtendimento.REALIZADO);
		atendimentoRepository.save(atendimento);
	}

	@Override
	public void validarAtendimento(Atendimento atendimento) {
		atendimento.setStatus(StatusAtendimento.VALIDADO);
		atendimentoRepository.save(atendimento);
	}

	@Override
	public List<Atendimento> buscarAtendimentosPorProfessor(Servidor servidor) {
		return atendimentoRepository.findAllByProfessor(servidor);
	}

	@Override
	public Boolean existeAtendimentoAbertoAlunoPaciente(Aluno aluno, Paciente paciente) {
		List<Atendimento> atendimentos = atendimentoRepository.findAllByResponsavelOrAjudanteExist(aluno, paciente,
				StatusAtendimento.EM_ANDAMENTO);
		return !atendimentos.isEmpty();
	}

	@Override
	public void remover(Atendimento atendimento) {
		atendimentoRepository.delete(atendimento);
	}

	@Override
	public Atendimento buscarUltimoAtendimentoAbertoPorAlunoEPaciente(Aluno aluno, Paciente paciente) {
		List<Atendimento> atendimentos = atendimentoRepository.findAllByResponsavelOrAjudanteExist(aluno, paciente,
				StatusAtendimento.EM_ANDAMENTO);
		if (atendimentos.isEmpty()) {
			return null;
		} else {
			return atendimentos.get(0);
		}
	}

	@Override
	public Atendimento adicionarAvaliacaoAtendimento(Atendimento atendimento) {
		if (atendimento.getAvaliacao() == null) {
			AvaliacaoAtendimento avaliacaoAtendimento = new AvaliacaoAtendimento();
			Avaliacao avaliacao = avaliacaoRepository.findAvaliacaoAtiva();
			if (avaliacao != null) {
				for (ItemAvaliacao item : avaliacao.getItens()) {
					ItemAvaliacaoAtendimento itemAvaliacao = new ItemAvaliacaoAtendimento();
					itemAvaliacao.setItemAvaliacao(item);
					itemAvaliacao.setAvaliacaoAtendimento(avaliacaoAtendimento);
					avaliacaoAtendimento.addItem(itemAvaliacao);
				}
				avaliacaoAtendimento.setAvaliacao(avaliacao);
				avaliacaoAtendimentoRepository.saveAndFlush(avaliacaoAtendimento);
				atendimento.setAvaliacao(avaliacaoAtendimento);
				atendimentoRepository.saveAndFlush(atendimento);
			}			
		}
		return atendimento;
	}

	public List<Atendimento> buscarAtendimentoPorPaciente(Paciente paciente) {
		List<Atendimento> atendimentos = new ArrayList<>();
		atendimentos.addAll(atendimentoRepository.findAllByPaciente(paciente));
		return atendimentos;
	}

	@Override
	public List<Atendimento> buscarAtendimentosPorUsuario(Integer idUsuario, Paciente paciente) {
		Aluno aluno = alunoRepository.findOne(idUsuario);
		Servidor servidor = servidorRepository.findOne(idUsuario);

		List<Atendimento> atendimentos = new ArrayList<>();
		if (aluno != null) {
			atendimentos = atendimentoRepository.buscarTodosPorPacienteEAluno(paciente, aluno);
		} else if (servidor != null) {
			atendimentos = atendimentoRepository.findAllByProfessorAndPaciente(servidor, paciente);
		}
		if (servidor != null && servidor.getPapeis().contains(Papel.ADMINISTRACAO)) {
			atendimentos = this.buscarAtendimentoPorPaciente(paciente);
		}
		if (atendimentos.isEmpty()) {
			atendimentos = atendimentoRepository.findAllByStatusAndPaciente(StatusAtendimento.VALIDADO, paciente);

		} else {

			List<Integer> idAtendimentos = new ArrayList<>();
			for (Atendimento atendimento : atendimentos) {
				idAtendimentos.add(atendimento.getId());
			}

			atendimentos.addAll(atendimentoRepository.findAllByStatusAndPacienteAndIdIsNotIn(StatusAtendimento.VALIDADO,
					paciente, idAtendimentos));
		}
		Collections.sort(atendimentos);

		return atendimentos;
	}

	@Override
	public void adicionarOdontograma(Atendimento atendimento) {
		Odontograma odontograma = odontogramaService.adicionar(atendimento);
		atendimento.getFichas().add(odontograma);
	}

	@Override
	public void adicionarPeriograma(Atendimento atendimento) throws ProntuarioException {
		Periograma periograma;
		periograma = periogramaService.adicionar(atendimento);
		atendimento.getFichas().add(periograma);
	}

	@Override
	public Atendimento buscarUltimoPorPacienteOdontograma(Paciente paciente) {
		List<Atendimento> lista = atendimentoRepository.findAllByPaciente(paciente);
		Collections.sort(lista);
		for (Atendimento atendimento : lista) {
			List<Ficha> fichas = atendimento.getFichas();
			for (Ficha f : fichas) {
				if (f instanceof Odontograma)
					return atendimento;
			}
		}
		return null;
	}

}
