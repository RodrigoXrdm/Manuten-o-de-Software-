package ufc.npi.prontuario.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Ficha;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.repository.OdontogramaRepository;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.OdontogramaService;

@Service
public class OdontogramaServiceImpl implements OdontogramaService {

	@Autowired
	private OdontogramaRepository odontogramaRepository;

	@Autowired
	private AtendimentoService atendimentoService;

	@Override
	public Odontograma buscarPorId(Integer id) {
		return odontogramaRepository.findOne(id);
	}

	@Override
	public Odontograma buscarPorAtendimento(Atendimento atendimento) {
		if (atendimento != null) {
			for (Ficha f : atendimento.getFichas()) {
				if (f instanceof Odontograma)
					return (Odontograma) f;
			}
			return new Odontograma();
		}
		return null;
	}

	@Override
	public void salvar(Odontograma odontograma) {
		odontogramaRepository.save(odontograma);
	}

	@Override
	public Odontograma adicionar(Atendimento atendimento) {
		Odontograma odontograma = new Odontograma();
		Odontograma anterior = null;
		Atendimento atendimentoAnteriorOdontograma = atendimentoService
				.buscarUltimoPorPacienteOdontograma(atendimento.getPaciente());
		if (atendimentoAnteriorOdontograma != null) {
			List<Ficha> fichas = atendimentoAnteriorOdontograma.getFichas();
			for (Ficha f : fichas) {
				if (f instanceof Odontograma)
					anterior = (Odontograma) f;
			}
		}

		odontograma.setAnterior(anterior);
		odontograma.setAtendimento(atendimento);

		salvar(odontograma);
		return odontograma;
	}

}
