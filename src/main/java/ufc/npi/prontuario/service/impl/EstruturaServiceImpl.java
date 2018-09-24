package ufc.npi.prontuario.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Arcada;
import ufc.npi.prontuario.model.Boca;
import ufc.npi.prontuario.model.Estrutura;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.enums.TipoArcada;
import ufc.npi.prontuario.repository.EstruturaRepository;
import ufc.npi.prontuario.service.EstruturaService;
import ufc.npi.prontuario.service.PacienteService;
import ufc.npi.prontuario.service.SextanteService;

@Service
public class EstruturaServiceImpl implements EstruturaService {

	@Autowired
	private EstruturaRepository estruturaRepository;

	@Autowired
	private SextanteService sextanteService;

	@Autowired
	private PacienteService pacienteService;

	@Override
	public void salvar(Estrutura estrutura) {
		estruturaRepository.save(estrutura);
	}

	@Override
	public Estrutura buscarPorId(Integer idEstrutura) {
		return estruturaRepository.findOne(idEstrutura);
	}

	@Override
	public void criarEstruturaBucal(Paciente paciente) {
		Boca boca = new Boca();

		Arcada superior = new Arcada();
		superior.setPaciente(paciente);
		superior.setBoca(boca);
		superior.setTipoArcada(TipoArcada.SUPERIOR);
		superior.setSextantes(sextanteService.criarSextantes(paciente, superior));

		Arcada inferior = new Arcada();
		inferior.setPaciente(paciente);
		inferior.setBoca(boca);
		inferior.setTipoArcada(TipoArcada.INFERIOR);
		inferior.setSextantes(sextanteService.criarSextantes(paciente, inferior));

		List<Arcada> arcadas = new ArrayList<>();
		arcadas.add(superior);
		arcadas.add(inferior);

		boca.setPaciente(paciente);
		boca.setArcadas(arcadas);
		this.salvar(boca);
		paciente.setBoca(boca);
		try {
			pacienteService.salvar(paciente);
		} catch (ProntuarioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
