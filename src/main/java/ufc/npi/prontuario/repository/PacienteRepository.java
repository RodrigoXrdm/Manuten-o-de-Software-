package ufc.npi.prontuario.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

	public Paciente findByCpf(@Param("cpf") String cpf);

	public Paciente findByCns(@Param("cns") String cns);

	public Paciente findByNomeAndNomeDaMaeAndNascimento(@Param("nome") String nome, @Param("nomeDaMae") String nomeDaMae, @Param("nascimento") Date nascimento);

	public List<Paciente> findByNomeContainingIgnoreCase(@Param("nome") String nome);

}
