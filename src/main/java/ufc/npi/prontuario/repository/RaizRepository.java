package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Raiz;

public interface RaizRepository  extends JpaRepository<Raiz, Integer>{
	List<Raiz> getByDente(Dente dente);
}
