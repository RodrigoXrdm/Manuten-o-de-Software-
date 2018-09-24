package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.enums.Papel;

public interface ServidorRepository extends JpaRepository<Servidor, Integer> {

	List<Servidor> findAllByOrderByNome();

    List<Servidor> findByPapeisIn(List<Papel> papeis);
}
