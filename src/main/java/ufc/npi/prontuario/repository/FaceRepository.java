package ufc.npi.prontuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Face;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.NumeroDente;

public interface FaceRepository  extends JpaRepository<Face, Integer>{

	@Query("SELECT COUNT(*) FROM Face f, Patologia p "
			+ "WHERE f.paciente.id = :pacienteId "
			+ "AND p.estrutura.id = f.id "
			+ "AND p.tipo.id = :tipoPatologiaId") 
	Integer getQtdFacesComTipoPatologia (@Param("pacienteId") Integer pacienteId, 
			@Param("tipoPatologiaId") Integer tipoPatologiaId);
	Face findByPacienteAndDenteNumeroAndNome(Paciente paciente, NumeroDente numeroDente, NomeFace nomeFace);
	
}
