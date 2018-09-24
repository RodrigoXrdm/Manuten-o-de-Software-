package ufc.npi.prontuario.service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Periograma;
import ufc.npi.prontuario.model.Sextante;
import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.NumeroDente;
import ufc.npi.prontuario.model.enums.StatusPeriograma;
import ufc.npi.prontuario.view.PeriogramaDTO;

public interface PeriogramaFase1Service {
	
	public StatusPeriograma atualizarStatusPeriograma(Periograma periograma, StatusPeriograma statusPeriograma);
	
	public Patologia salvarPsr(Periograma periograma, Sextante sextante,  String descricao) throws ProntuarioException;
	
	public Patologia salvarCalculoSupragengival(Periograma periograma, Paciente paciente, NumeroDente numeroDente) throws ProntuarioException;
	
	public void removerCalculoSupragengival(Periograma periograma, Paciente paciente, NumeroDente numeroDente);
	
	public Patologia salvarOutrosFatores(Periograma periograma, Paciente paciente, NumeroDente numeroDente, String descricao) throws ProntuarioException;

	public Patologia salvarSangramentoMarginal(Periograma periograma, Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace) throws ProntuarioException;
	
	public void removerSangramentoMarginal(Periograma periograma, Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace);
	
	public Patologia salvarPlacaBacteriana(Periograma periograma, Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace) throws ProntuarioException;
	
	public void removerPlacaBacteriana(Periograma periograma, Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace);
	
	public String calcularIndices(Paciente paciente) throws ProntuarioException;
	
	public PeriogramaDTO carregarPeriograma(Periograma periograma);
}
