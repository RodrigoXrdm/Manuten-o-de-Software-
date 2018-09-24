package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PERIOGRAMA_JA_CADASTRADO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.NENHUM_ATENDIMENTO_ABERTO_EXCEPTION;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Ficha;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.enums.StatusAtendimento;
import ufc.npi.prontuario.model.enums.StatusPeriograma;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.PeriogramaRepository;
import ufc.npi.prontuario.service.PeriogramaService;;

@Service
public class PeriogramaServiceImpl implements PeriogramaService {

	@Autowired
	private PeriogramaRepository periogramaRepository;

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	@Override
	public Periograma adicionar(Paciente paciente, Aluno aluno, StatusPeriograma statusPeriograma)
			throws ProntuarioException {

		Atendimento atendimento = atendimentoRepository
				.findByStatusAndPacienteAndResponsavel(StatusAtendimento.EM_ANDAMENTO, paciente, aluno);
		if (atendimento == null) {
			throw new ProntuarioException(NENHUM_ATENDIMENTO_ABERTO_EXCEPTION);
		}

		for (Ficha ficha : atendimento.getFichas()) {
			if (ficha instanceof Periograma) {
				throw new ProntuarioException(ERRO_PERIOGRAMA_JA_CADASTRADO);
			}
		}

		Periograma periograma = new Periograma();
		periograma.setAtendimento(atendimento);
		periograma.setStatus(statusPeriograma);
		atendimento.getFichas().add(periograma);

		periogramaRepository.save(periograma);
		atendimentoRepository.save(atendimento);
		return periograma;
	}

	@Override
	public Periograma adicionar(Atendimento atendimento) throws ProntuarioException {

		for (Ficha ficha : atendimento.getFichas()) {
			if (ficha instanceof Periograma) {
				throw new ProntuarioException(ERRO_PERIOGRAMA_JA_CADASTRADO);
			}
		}

		Periograma periograma = new Periograma();
		periograma.setAtendimento(atendimento);
		periograma.setStatus(StatusPeriograma.EXAME_INICIAL);
		atendimento.getFichas().add(periograma);

		periogramaRepository.save(periograma);
		atendimentoRepository.save(atendimento);
		return periograma;
	}

	@Override
	public void atualizar(Periograma periograma) {
		periogramaRepository.save(periograma);
	}

	@Override
	public Periograma buscarPorId(Integer id) {
		return periogramaRepository.findOne(id);
	}

	@Override
	public Periograma buscarPorAtendimento(Atendimento atendimento) {
		if (atendimento != null) {
			for (Ficha f : atendimento.getFichas()) {
				if (f instanceof Odontograma)
					return (Periograma) f;
			}
		}
		return null;
	}

	@Override
	public List<Periograma> buscarPeriogramasValidados(Paciente paciente) {
		List<Periograma> periogramas = new ArrayList<>();
		periogramaRepository.findAllByAtendimentoStatusAndAtendimentoPacienteOrderByAtendimentoDataDesc(
				StatusAtendimento.VALIDADO, paciente);
		return periogramas;
	}

	@Override
	public boolean existePeriogramaJaCadastrado(Paciente paciente, Aluno aluno) {
		Atendimento atendimento = atendimentoRepository
				.findByStatusAndPacienteAndResponsavel(StatusAtendimento.EM_ANDAMENTO, paciente, aluno);

		if (atendimento != null) {
			for (Ficha ficha : atendimento.getFichas()) {
				if (ficha instanceof Periograma) {
					return true;
				}
			}
		}
		return false;
	}
}
