package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.repository.TipoPatologiaRepository;
import ufc.npi.prontuario.service.TipoPatologiaService;
import ufc.npi.prontuario.util.ExceptionSuccessConstants;

@Service
public class TipoPatologiaServiceImpl implements TipoPatologiaService {

	@Autowired
	private TipoPatologiaRepository tipoPatologiaRepository;

	@Override
	public void salvar(TipoPatologia tipoPatologia) throws ProntuarioException {
		if (tipoPatologia.getNome().trim().isEmpty() || tipoPatologia.getDescricao().trim().isEmpty() 
				|| tipoPatologia.getCodigo().trim().isEmpty()) {
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}

		if (tipoPatologiaRepository.findByNome(tipoPatologia.getNome()) != null) {
			throw new ProntuarioException(ExceptionSuccessConstants.ERRO_SALVAR_TIPO_PATOLOGIA_EXISTENTE);
		}
		tipoPatologiaRepository.save(tipoPatologia);
	}

	@Override
	public List<TipoPatologia> buscarTudo() {
		return tipoPatologiaRepository.findAll();
	}

	@Override
	public TipoPatologia buscarPorId(Integer id) {
		return tipoPatologiaRepository.findOne(id);
	}

	@Override
	public void remover(Integer id) throws Exception {
		tipoPatologiaRepository.delete(id);
	}

	@Override
	public List<TipoPatologia> buscarPorNome(String nome) {
		return tipoPatologiaRepository.findByNomeContainingIgnoreCase(nome);
	}

	@Override
	public TipoPatologia buscarUmTipoPatologiaPorNome(String nome) {
		return tipoPatologiaRepository.findByNome(nome);
	}

	@Override
	public void atualizar(TipoPatologia tipoPatologia) throws ProntuarioException {
		if (tipoPatologia.getId() == null || tipoPatologia.getNome().trim().isEmpty() 
				|| tipoPatologia.getDescricao().trim().isEmpty() || tipoPatologia.getCodigo().trim().isEmpty()) {
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}
		TipoPatologia tipoPatologiaExistente = tipoPatologiaRepository.findByNome(tipoPatologia.getNome());
		if (tipoPatologiaExistente != null && !tipoPatologiaExistente.getId().equals(tipoPatologia.getId())) {
			throw new ProntuarioException(ExceptionSuccessConstants.ERRO_SALVAR_TIPO_PATOLOGIA_EXISTENTE);
		}

		tipoPatologiaRepository.save(tipoPatologia);
	}
}
