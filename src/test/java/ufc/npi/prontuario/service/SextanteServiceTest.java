package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.model.Arcada;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Sextante;
import ufc.npi.prontuario.model.enums.NomeSextante;

@DatabaseSetup(SextanteServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { SextanteServiceTest.DATASET })
public class SextanteServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-sextante.xml";

	@Autowired
	private SextanteService sextanteService;

	@Autowired
	private EstruturaService arcadaService;

	@Autowired
	private PacienteService pacienteService;

	@Test
	public void testFindAllByPacienteOrderByNomeAsc() {
		Paciente paciente = pacienteService.buscarPorId(1);
		List<Sextante> sextantes = sextanteService.findAllByPacienteOrderByNomeAsc(paciente);
		assertEquals(6, sextantes.size());
	}

	@Test
	public void testCriarSextantesArcadaSuperior() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Arcada arcada = (Arcada) arcadaService.buscarPorId(3);
		List<Sextante> sextantes = sextanteService.criarSextantes(paciente, arcada);
		assertEquals(3, sextantes.size());

		Sextante sextante1 = sextantes.get(0);
		Sextante sextante2 = sextantes.get(1);
		Sextante sextante3 = sextantes.get(2);

		assertEquals(arcada, sextante1.getArcada());
		assertEquals(arcada, sextante2.getArcada());
		assertEquals(arcada, sextante3.getArcada());

		assertEquals(NomeSextante.S1, sextante1.getNome());
		assertEquals(NomeSextante.S2, sextante2.getNome());
		assertEquals(NomeSextante.S3, sextante3.getNome());

		assertEquals(paciente.getId(), sextante1.getPaciente().getId());
		assertEquals(paciente.getId(), sextante2.getPaciente().getId());
		assertEquals(paciente.getId(), sextante3.getPaciente().getId());

	}

	@Test
	public void testCriarSextantesArcadaInferior() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Arcada arcada = (Arcada) arcadaService.buscarPorId(4);
		List<Sextante> sextantes = sextanteService.criarSextantes(paciente, arcada);
		assertEquals(3, sextantes.size());

		Sextante sextante1 = sextantes.get(0);
		Sextante sextante2 = sextantes.get(1);
		Sextante sextante3 = sextantes.get(2);

		assertEquals(arcada, sextante1.getArcada());
		assertEquals(arcada, sextante2.getArcada());
		assertEquals(arcada, sextante3.getArcada());

		assertEquals(NomeSextante.S4, sextante1.getNome());
		assertEquals(NomeSextante.S5, sextante2.getNome());
		assertEquals(NomeSextante.S6, sextante3.getNome());

		assertEquals(paciente.getId(), sextante1.getPaciente().getId());
		assertEquals(paciente.getId(), sextante2.getPaciente().getId());
		assertEquals(paciente.getId(), sextante3.getPaciente().getId());
	}

}
