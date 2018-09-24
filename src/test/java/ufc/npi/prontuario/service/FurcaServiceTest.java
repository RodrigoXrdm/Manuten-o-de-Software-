package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Sextante;

@DatabaseSetup(FurcaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { FurcaServiceTest.DATASET })
public class FurcaServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-furca.xml";

	@Autowired
	private FurcaService furcaService;

	@Autowired
	private SextanteService sextanteService;

	@Autowired
	private PacienteService pacienteService;

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testCriarFurcasParaDenteComTresRaizes() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Sextante sextante = sextanteService.findAllByPacienteOrderByNomeAsc(paciente).get(0);
		Dente dente = sextante.getDentes().get(0);
		furcaService.criarFurcas(dente);

		assertEquals(3, dente.getRaizes().size());
		assertEquals(2, dente.getRaizes().get(0).getFurcas().size());
		assertEquals(2, dente.getRaizes().get(1).getFurcas().size());
		assertEquals(2, dente.getRaizes().get(2).getFurcas().size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testCriarFurcasDenteDuasRaizes() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Sextante sextante = sextanteService.findAllByPacienteOrderByNomeAsc(paciente).get(0);
		Dente dente = sextante.getDentes().get(1);
		furcaService.criarFurcas(dente);

		assertEquals(2, dente.getRaizes().size());
		assertEquals(2, dente.getRaizes().get(0).getFurcas().size());
		assertEquals(2, dente.getRaizes().get(1).getFurcas().size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testCriarFurcasDentesUmaRaiz() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Sextante sextante = sextanteService.findAllByPacienteOrderByNomeAsc(paciente).get(0);
		Dente dente = sextante.getDentes().get(3);
		furcaService.criarFurcas(dente);

		assertEquals(1, dente.getRaizes().size());
		assertEquals(0, dente.getRaizes().get(0).getFurcas().size());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testCriarFurcasDentesDuasRaizesComFaceMesialOclusal() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Sextante sextante = sextanteService.findAllByPacienteOrderByNomeAsc(paciente).get(0);
		Dente dente = sextante.getDentes().get(2);
		furcaService.criarFurcas(dente);

		assertEquals(2, dente.getRaizes().size());
		assertEquals(2, dente.getRaizes().get(0).getFurcas().size());
		assertEquals(2, dente.getRaizes().get(1).getFurcas().size());
	}

}
