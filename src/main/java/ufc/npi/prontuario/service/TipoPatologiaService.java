package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoPatologia;

public interface TipoPatologiaService {

	void salvar(TipoPatologia tipoPatologia) throws ProntuarioException;

	void atualizar(TipoPatologia tipoPatologia) throws ProntuarioException;

	List<TipoPatologia> buscarTudo();

	List<TipoPatologia> buscarPorNome(String nome);

	TipoPatologia buscarPorId(Integer id);

	void remover(Integer id) throws Exception;

	public TipoPatologia buscarUmTipoPatologiaPorNome(String nome);

}
