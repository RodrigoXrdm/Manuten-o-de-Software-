package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_TIPO_PATOLOGIA_INEXISTENTE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Furca;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.repository.DenteRepository;
import ufc.npi.prontuario.repository.PatologiaRepository;
import ufc.npi.prontuario.repository.TipoPatologiaRepository;
import ufc.npi.prontuario.service.PeriogramaFase2Service;

@Service
public class PeriogramaFase2ServiceImpl implements PeriogramaFase2Service {

	@Autowired
	private PatologiaRepository patologiaRepository;

	@Autowired
	private TipoPatologiaRepository tipoPatologiaRepository;

	@Autowired
	private DenteRepository denteRepository;

	public Patologia salvarMobilidade(Periograma periograma, Dente dente, String descricao) throws ProntuarioException{
		Patologia mobilidade = new Patologia();

		TipoPatologia tipoMobilidade = tipoPatologiaRepository.findByNome("Mobilidade");

		if(tipoMobilidade != null){
			mobilidade.setTipo(tipoMobilidade);
			mobilidade.setEstrutura(dente);
			mobilidade.setFicha(periograma);
			mobilidade.setDescricao(descricao);

			return patologiaRepository.save(mobilidade);
		}
		else{
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}
	}


	public Patologia salvarImplante(Periograma periograma, Dente dente) throws ProntuarioException{
		Patologia implante = new Patologia();

		TipoPatologia tipoImplante = tipoPatologiaRepository.findByNome("Implante");

		if(tipoImplante != null){
			implante.setTipo(tipoImplante);
			implante.setEstrutura(dente);
			implante.setFicha(periograma);

			dente.setImplante(true);
			denteRepository.save(dente);

			return patologiaRepository.save(implante);
		}

		else{
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}

	}


	public Patologia salvarFurca(Periograma periograma, Furca furca) throws ProntuarioException{
		Patologia lesaoFurca = new Patologia();

		TipoPatologia tipoFurca = tipoPatologiaRepository.findByNome("Lesão de Furca");

		if(tipoFurca != null){
			lesaoFurca.setTipo(tipoFurca);
			lesaoFurca.setEstrutura(furca);
			lesaoFurca.setFicha(periograma);
			
			//TODO inserir patologia na estrutura da furca e dente

			return patologiaRepository.save(lesaoFurca);
		}

		else{
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}
	}



}
