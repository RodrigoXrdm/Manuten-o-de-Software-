package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PACIENTE_CNS_EXISTENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PACIENTE_CPF_EXISTENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PACIENTE_EXISTENTE;

import java.text.Normalizer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PacienteAnamnese;
import ufc.npi.prontuario.repository.PacienteAnamneseRepository;
import ufc.npi.prontuario.repository.PacienteRepository;
import ufc.npi.prontuario.service.PacienteService;

@Service
public class PacienteServiceImpl implements PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private PacienteAnamneseRepository pacienteAnamneseRepository;

	@Override
	public void salvar(Paciente paciente) throws ProntuarioException {

		if (paciente.getCpf() != null && !paciente.getCpf().isEmpty()) {
			Paciente pacienteExistente = buscarPorCpf(paciente.getCpf());

			if (pacienteExistente != null && !pacienteExistente.equals(paciente)) {
				throw new ProntuarioException(ERRO_PACIENTE_CPF_EXISTENTE);
			}
		}

		if (paciente.getSexo() == null || paciente.getNascimento() == null || paciente.getNomeDaMae() == null || paciente.getNomeDaMae().isEmpty() || paciente.getNome() == null
				|| paciente.getNome().isEmpty() || paciente.getTelefone() == null || paciente.getTelefone().isEmpty()) {
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}

		if (!paciente.getNome().isEmpty() && !paciente.getNomeDaMae().isEmpty()) {
			Paciente pacienteExistente = pacienteRepository.findByNomeAndNomeDaMaeAndNascimento(removerAcentos(paciente.getNome().toUpperCase()), removerAcentos(paciente.getNomeDaMae().toUpperCase()),
					paciente.getNascimento());

			if (pacienteExistente != null) {
				throw new ProntuarioException(ERRO_PACIENTE_EXISTENTE);
			}
		}

		if (paciente.getCns() != null && !paciente.getCns().isEmpty()) {
			Paciente pacienteExistente = pacienteRepository.findByCns(paciente.getCns());

			if (pacienteExistente != null && !pacienteExistente.equals(paciente)) {
				throw new ProntuarioException(ERRO_PACIENTE_CNS_EXISTENTE);
			}
		}

		paciente.setNome(removerAcentos(paciente.getNome().toUpperCase()));
		paciente.setNomeDaMae(removerAcentos(paciente.getNomeDaMae().toUpperCase()));
		pacienteRepository.save(paciente);
	}

	@Override
	public List<Paciente> buscarTudo() {
		return pacienteRepository.findAll();
	}

	@Override
	public Paciente buscarPorCpf(String cpf) {
		return pacienteRepository.findByCpf(cpf);
	}

	@Override
	public Paciente buscarPorId(Integer id) {
		return pacienteRepository.findOne(id);
	}

	@Override
	public Paciente buscarPorCns(String cns) {
		return pacienteRepository.findByCns(cns);
	}

	@Override
	public void adicionarAnamnese(Paciente paciente, PacienteAnamnese pacienteAnamnese) {
		paciente.addPacienteAnamnese(pacienteAnamnese);
		pacienteRepository.save(paciente);
	}

	@Override
	public PacienteAnamnese buscarPacienteAnamnesePorId(Integer id) {
		return pacienteAnamneseRepository.findOne(id);
	}

	@Override
	public List<PacienteAnamnese> buscarPacienteAnamneses(Paciente paciente) {
		return pacienteAnamneseRepository.buscarPorPaciente(paciente);
	}

	@Override
	public List<Paciente> buscarPorNome(String nome) {
		nome = this.removerAcentos(nome);
		List<Paciente> pacientes = pacienteRepository.findByNomeContainingIgnoreCase(nome);
		return pacientes;
	}

	private String removerAcentos(String nome) {
		nome = Normalizer.normalize(nome, Normalizer.Form.NFD);
		nome = nome.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return nome;
	}

}
