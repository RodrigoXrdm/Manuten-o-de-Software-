package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ufc.npi.prontuario.model.Registro;


public interface RegistroRepository extends JpaRepository<Registro, Integer> {

	
	@Query("SELECT new ufc.npi.prontuario.model.Registro(r.id, r.data, r.idUsuario, r.idPaciente, r.tipoAcao) FROM Registro r")
	List<Registro> findAllSample();
	
	Registro findById(Integer id);

}
