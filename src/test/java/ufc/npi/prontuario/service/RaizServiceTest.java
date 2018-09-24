package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Raiz;
import ufc.npi.prontuario.model.enums.NomeRaiz;

@DatabaseSetup(RaizServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { RaizServiceTest.DATASET })
public class RaizServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-raiz.xml";

	@Autowired
	private RaizService raizService;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private EstruturaService estruturaService;

	@Test
	public void testBuscarRaizPorDente() {
		Dente dente = (Dente) estruturaService.buscarPorId(26);
		List<Raiz> raizes = raizService.getByDente(dente);
		assertEquals(2, raizes.size());
		assertEquals(NomeRaiz.R1, raizes.get(0).getNome());
		assertEquals(NomeRaiz.R2, raizes.get(1).getNome());
		assertEquals(dente, raizes.get(0).getDente());
		assertEquals(dente, raizes.get(1).getDente());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testCriarParaDenteComTresRaizes() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Dente dente = (Dente) estruturaService.buscarPorId(5);
		List<Raiz> raizes = raizService.criarRaizes(paciente, dente);
		assertEquals(3, raizes.size());
		assertEquals(NomeRaiz.R1, raizes.get(0).getNome());
		assertEquals(NomeRaiz.R2, raizes.get(1).getNome());
		assertEquals(NomeRaiz.R3, raizes.get(2).getNome());
		assertEquals(paciente.getId(), raizes.get(0).getPaciente().getId());
		assertEquals(paciente.getId(), raizes.get(1).getPaciente().getId());
		assertEquals(paciente.getId(), raizes.get(2).getPaciente().getId());
		assertEquals(dente, raizes.get(0).getDente());
		assertEquals(dente, raizes.get(1).getDente());
		assertEquals(dente, raizes.get(2).getDente());

	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testCriarParaDenteComDuasRaizes() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Dente dente = (Dente) estruturaService.buscarPorId(6);
		raizService.criarRaizes(paciente, dente);
		List<Raiz> raizes = raizService.criarRaizes(paciente, dente);
		assertEquals(2, raizes.size());
		assertEquals(NomeRaiz.R1, raizes.get(0).getNome());
		assertEquals(NomeRaiz.R2, raizes.get(1).getNome());
		assertEquals(paciente.getId(), raizes.get(0).getPaciente().getId());
		assertEquals(paciente.getId(), raizes.get(1).getPaciente().getId());
		assertEquals(dente, raizes.get(0).getDente());
		assertEquals(dente, raizes.get(1).getDente());

	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testCriarParaDenteComUmaRaiz() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Dente dente = (Dente) estruturaService.buscarPorId(7);
		raizService.criarRaizes(paciente, dente);
		List<Raiz> raizes = raizService.criarRaizes(paciente, dente);
		assertEquals(1, raizes.size());
		assertEquals(NomeRaiz.R1, raizes.get(0).getNome());
		assertEquals(paciente.getId(), raizes.get(0).getPaciente().getId());
		assertEquals(dente, raizes.get(0).getDente());
	}

}
