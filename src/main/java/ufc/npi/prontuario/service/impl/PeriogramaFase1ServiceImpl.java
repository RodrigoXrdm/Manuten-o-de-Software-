package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_TIPO_PATOLOGIA_INEXISTENTE;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Face;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.Sextante;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.NumeroDente;
import ufc.npi.prontuario.model.enums.StatusPeriograma;
import ufc.npi.prontuario.repository.ArcadaRepository;
import ufc.npi.prontuario.repository.DenteRepository;
import ufc.npi.prontuario.repository.FaceRepository;
import ufc.npi.prontuario.repository.PatologiaRepository;
import ufc.npi.prontuario.repository.PeriogramaRepository;
import ufc.npi.prontuario.repository.TipoPatologiaRepository;
import ufc.npi.prontuario.service.PeriogramaFase1Service;
import ufc.npi.prontuario.view.PeriogramaDTO;


@Service
public class PeriogramaFase1ServiceImpl implements PeriogramaFase1Service {

	@Autowired
	private PatologiaRepository patologiaRepository;

	@Autowired
	private TipoPatologiaRepository tipoPatologiaRepository;

	@Autowired
	private DenteRepository denteRepository;

	@Autowired
	private FaceRepository faceRepository;
	
	@Autowired
	private PeriogramaRepository periogramaRepository;
	
	@Autowired
	private ArcadaRepository arcadaRepository;
	
	@Override
	public StatusPeriograma atualizarStatusPeriograma(Periograma periograma, StatusPeriograma statusPeriograma) {
		periograma.setStatus(statusPeriograma);
		periogramaRepository.save(periograma);
		return statusPeriograma;
	}
	
	@Override
	public Patologia salvarPsr(Periograma periograma, Sextante sextante, String descricao) throws ProntuarioException{
		
		TipoPatologia tipoPsr = tipoPatologiaRepository.findByNome("PSR");
		
		Patologia psr = Optional.ofNullable(patologiaRepository.findByEstruturaAndFichaAndTipo(sextante, periograma, tipoPsr)).orElse(new Patologia());
		
		if(descricao.equals("")) {
			patologiaRepository.delete(psr);
			return null;
		}
		
		if(tipoPsr == null) {
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}
		
		psr.setTipo(tipoPsr);
		psr.setEstrutura(sextante);
		psr.setFicha(periograma);
		psr.setData(new Date(System.currentTimeMillis()));
		psr.setDescricao(descricao);
		
		return patologiaRepository.save(psr);
		
	}
	
	@Override
	public Patologia salvarCalculoSupragengival(Periograma periograma, Paciente paciente, NumeroDente numeroDente) throws ProntuarioException{
		Dente dente = denteRepository.findByNumeroAndPaciente(numeroDente, paciente);
		
		TipoPatologia tipoCalculoSupragengival = tipoPatologiaRepository.findByNome("Cálculo Supragengival");
		
		Patologia calculoSupragengival = Optional.ofNullable(patologiaRepository.findByEstruturaAndFichaAndTipo(dente, periograma, tipoCalculoSupragengival)).orElse(new Patologia());
				
		if(tipoCalculoSupragengival != null){
			calculoSupragengival.setTipo(tipoCalculoSupragengival);
			calculoSupragengival.setEstrutura(dente);
			calculoSupragengival.setData(new Date(System.currentTimeMillis()));
			calculoSupragengival.setFicha(periograma);
			
			return patologiaRepository.save(calculoSupragengival);
		}
		else{
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}
	}
	
	@Override
	public void removerCalculoSupragengival(Periograma periograma, Paciente paciente, NumeroDente numeroDente) {
		TipoPatologia tipoCalculoSupragengival = tipoPatologiaRepository.findByNome("Cálculo Supragengival");
		Dente dente = denteRepository.findByNumeroAndPaciente(numeroDente, paciente);
		Patologia calculoSupragengival = patologiaRepository.findByEstruturaAndFichaAndTipo(dente, periograma, tipoCalculoSupragengival);
		patologiaRepository.delete(calculoSupragengival);
	}


	@Override
	public Patologia salvarOutrosFatores(Periograma periograma, Paciente paciente, NumeroDente numeroDente, String descricao) throws ProntuarioException{
		Dente dente = denteRepository.findByNumeroAndPaciente(numeroDente, paciente);

		TipoPatologia tipoOutrosFatores = tipoPatologiaRepository.findByNome("Outros Fatores");
		
		Patologia outrosFatores = Optional.ofNullable(patologiaRepository.findByEstruturaAndFichaAndTipo(dente, periograma, tipoOutrosFatores)).orElse(new Patologia());

		if(tipoOutrosFatores == null){
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}
		
		if(!descricao.equals("")){
			outrosFatores.setTipo(tipoOutrosFatores);
			outrosFatores.setEstrutura(dente);
			outrosFatores.setFicha(periograma);
			outrosFatores.setData(new Date(System.currentTimeMillis()));
			outrosFatores.setDescricao(descricao);

			return patologiaRepository.save(outrosFatores);
		} else {
			patologiaRepository.delete(outrosFatores);
			return null;
		}
	}

	@Override
	public Patologia salvarSangramentoMarginal(Periograma periograma, Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace) throws ProntuarioException{
		
		Face face = faceRepository.findByPacienteAndDenteNumeroAndNome(paciente, numeroDente, nomeFace);

		TipoPatologia tipoSangramentoMarginal = tipoPatologiaRepository.findByNome("Sangramento Marginal");
			
		Patologia sangramentoMarginal = Optional.ofNullable(patologiaRepository.findByEstruturaAndFichaAndTipo(face, periograma, tipoSangramentoMarginal)).orElse(new Patologia());
		
		if(tipoSangramentoMarginal == null){
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}	
		
		sangramentoMarginal.setTipo(tipoSangramentoMarginal);
		sangramentoMarginal.setData(new Date(System.currentTimeMillis()));
		sangramentoMarginal.setFicha(periograma);
		sangramentoMarginal.setEstrutura(face);

		return patologiaRepository.save(sangramentoMarginal);

	}
	
	@Override
	public void removerSangramentoMarginal(Periograma periograma, Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace) {
		Face face = faceRepository.findByPacienteAndDenteNumeroAndNome(paciente, numeroDente, nomeFace);
		TipoPatologia tipoSangramentoMarginal = tipoPatologiaRepository.findByNome("Sangramento Marginal");
		Patologia sangramentoMarginal = patologiaRepository.findByEstruturaAndFichaAndTipo(face, periograma, tipoSangramentoMarginal);
		
		patologiaRepository.delete(sangramentoMarginal);
	}

	@Override
	public Patologia salvarPlacaBacteriana(Periograma periograma, Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace) throws ProntuarioException{
		Face face = faceRepository.findByPacienteAndDenteNumeroAndNome(paciente, numeroDente, nomeFace);

		TipoPatologia tipoSangramentoMarginal = tipoPatologiaRepository.findByNome("Placa Bacteriana");
			
		Patologia placaBacteriana = Optional.ofNullable(patologiaRepository.findByEstruturaAndFichaAndTipo(face, periograma, tipoSangramentoMarginal)).orElse(new Patologia());
		
		if(tipoSangramentoMarginal == null){
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}	
		
		placaBacteriana.setTipo(tipoSangramentoMarginal);
		placaBacteriana.setData(new Date(System.currentTimeMillis()));
		placaBacteriana.setFicha(periograma);
		placaBacteriana.setEstrutura(face);

		return patologiaRepository.save(placaBacteriana);
	}
	
	
	@Override
	public void removerPlacaBacteriana(Periograma periograma, Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace) {
		Face face = faceRepository.findByPacienteAndDenteNumeroAndNome(paciente, numeroDente, nomeFace);
		TipoPatologia tipoPlacaBacteriana = tipoPatologiaRepository.findByNome("Placa Bacteriana");
		Patologia placaBacteriana = patologiaRepository.findByEstruturaAndFichaAndTipo(face, periograma, tipoPlacaBacteriana);
		
		patologiaRepository.delete(placaBacteriana);
	}

	@Override
	public String calcularIndices(Paciente paciente) throws ProntuarioException{

		TipoPatologia sangramentoMarginal = tipoPatologiaRepository.findByNome("Sangramento Marginal");
		TipoPatologia placaBacteriana = tipoPatologiaRepository.findByNome("Placa Bacteriana");

		if(sangramentoMarginal == null || placaBacteriana == null){
			throw new ProntuarioException(ERRO_TIPO_PATOLOGIA_INEXISTENTE);
		}
		
		else{
			
			Integer numeroDeDentes = denteRepository.getQtdDentesPresenteFromPaciente(paciente.getId());
			Integer facesComSangramentoMarginal = faceRepository.getQtdFacesComTipoPatologia(paciente.getId(), sangramentoMarginal.getId());
			Integer facesComPlacaBacteriana = faceRepository.getQtdFacesComTipoPatologia(paciente.getId(), placaBacteriana.getId());

			float indiceSangramentoMarginal = 0;
			float indicePlacaBacteriana = 0;
			float placaOLeary = 0;
			
			if (facesComSangramentoMarginal != 0){
				indiceSangramentoMarginal = (float) ((numeroDeDentes * 4) / facesComSangramentoMarginal) / 100;
			}
			
			if(facesComPlacaBacteriana != 0){
				indicePlacaBacteriana = (float) ((numeroDeDentes * 4) / facesComPlacaBacteriana) / 100;
			}
			
			if (facesComPlacaBacteriana != 0 && facesComSangramentoMarginal != 0){
				placaOLeary = (float) ((facesComSangramentoMarginal / facesComPlacaBacteriana) * 100) / numeroDeDentes * 4;
			}

			return indiceSangramentoMarginal + "," + indicePlacaBacteriana + "," + placaOLeary; 
		}

	}

	@Override
	public PeriogramaDTO carregarPeriograma(Periograma periograma) {
		PeriogramaDTO dto = new PeriogramaDTO();
		Paciente paciente = periograma.getAtendimento().getPaciente();
		
		dto.setPatologias(periograma.getPatologias());
		dto.setDentes(denteRepository.getByPaciente(paciente));
		dto.setArcadas(arcadaRepository.getByPaciente(paciente));
		
		return dto;
	}

}