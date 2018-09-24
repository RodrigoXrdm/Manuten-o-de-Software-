package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_EDITAR_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_EXCLUIR_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_FINALIZAR_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADD_PLANO_TRATAMENTO;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.enums.StatusPlanoTratamento;
import ufc.npi.prontuario.repository.PlanoTratamentoRepository;
import ufc.npi.prontuario.service.PlanoTratamentoService;

@Service
public class PlanoTratamentoServiceImpl implements PlanoTratamentoService {

	@Autowired
	private PlanoTratamentoRepository planoTratamentoRepository;

	@Override
	public List<PlanoTratamento> buscarPlanoTratamentoPorPaciente(Paciente paciente) {
		return planoTratamentoRepository.findAllByPaciente(paciente);
	}

	@Override
	public PlanoTratamento buscarPlanoPorId(Integer id) {
		return planoTratamentoRepository.findOne(id);
	}

	@Override
	public void excluirPlanoTratamento(PlanoTratamento planoTratamento) throws ProntuarioException {
		PlanoTratamento old = planoTratamentoRepository.findOne(planoTratamento.getId());
		if (!old.getStatus().equals(StatusPlanoTratamento.EM_ESPERA)) {
			throw new ProntuarioException(ERROR_EXCLUIR_PLANO_TRATAMENTO);
		} else {
			planoTratamentoRepository.delete(old);
		}
	}

	@Override
	public void salvar(PlanoTratamento planoTratamento, Servidor responsavel, Paciente paciente)
			throws ProntuarioException {
		List<StatusPlanoTratamento> statuses = Arrays.asList(StatusPlanoTratamento.EM_ESPERA,
				StatusPlanoTratamento.EM_ANDAMENTO);
		Integer temTratamento = planoTratamentoRepository.countByPacienteAndClinicaAndStatusIn(
				planoTratamento.getPaciente(), planoTratamento.getClinica(), statuses);

		if (temTratamento == 0) {
			planoTratamento.setResponsavel(responsavel);
			planoTratamento.setPaciente(paciente);
			planoTratamentoRepository.save(planoTratamento);
		} else {
			throw new ProntuarioException(ERRO_ADD_PLANO_TRATAMENTO);
		}
	}

	@Override
	public void finalizar(PlanoTratamento planoTratamento) throws ProntuarioException {
		PlanoTratamento planoAux = planoTratamentoRepository.findOne(planoTratamento.getId());
		if (planoAux.getStatus().equals(StatusPlanoTratamento.EM_ESPERA)) {
			planoAux.setStatus(StatusPlanoTratamento.CONCLUIDO);
			planoTratamentoRepository.save(planoAux);
		} else {
			throw new ProntuarioException(ERROR_FINALIZAR_PLANO_TRATAMENTO);
		}

	}

	@Override
	public List<PlanoTratamento> buscarPlanoTratamentoPorClinicaEStatus(Disciplina disciplina, String status) {
		if (!status.equalsIgnoreCase("")) {
			StatusPlanoTratamento statusPlanoTratamento = StatusPlanoTratamento.valueOf(status.trim());
			return planoTratamentoRepository.findByClinicaAndStatus(disciplina, statusPlanoTratamento);
		} else {
			return planoTratamentoRepository.findByClinica(disciplina);
		}

	}

	@Override
	public void editar(PlanoTratamento planoTratamento) throws ProntuarioException {
		PlanoTratamento old = planoTratamentoRepository.findOne(planoTratamento.getId());
		if (old == null || (old.getStatus().equals(StatusPlanoTratamento.CONCLUIDO)
				|| old.getStatus().equals(StatusPlanoTratamento.INTERROMPIDO))) {
			throw new ProntuarioException(ERROR_EDITAR_PLANO_TRATAMENTO);
		} else {
			planoTratamentoRepository.save(planoTratamento);
		}
	}

}
